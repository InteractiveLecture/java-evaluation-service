package main

import (
	"log"
	"net/http"
	"net/url"
	"testing"
	"time"

	"github.com/InteractiveLecture/testframework"
	"github.com/gorilla/websocket"
	"github.com/satori/go.uuid"
	"github.com/sergi/go-diff/diffmatchpatch"
	"github.com/stretchr/testify/require"
)

func TestIntegration(t *testing.T) {
	officerUsername := testframework.RegisterNewUser(t, "officer")
	userUsername := testframework.RegisterNewUser(t, "user")
	taskId := uuid.NewV4().String()
	sources := map[string]string{
		"HalloWelt": `public class HalloWelt {
			public static void main(String[]args){
				System.out.println("hallo welt!");
			}
		}`,
	}
	containerId := addTestContainer(t, officerUsername, taskId, sources)
	resp := testframework.GetAuthorized(t, officerUsername, "/java-evaluation-service/tests/"+containerId, "User-Id", officerUsername)
	require.Equal(t, http.StatusOK, resp.StatusCode)
	resultContainer := testframework.ReadSingleJsonResult(t, resp)
	require.Equal(t, containerId, resultContainer["id"])
	require.Equal(t, sources["HalloWelt"], resultContainer["sources"].(map[string]interface{})["HalloWelt"])

	newSource := `public class HalloWelt {
		public static void main(String[] args) {
			System.out.println("Hugo!");
		}
	}`

	compileErrors := patchTestSource(t, officerUsername, containerId, "HalloWelt", sources["HalloWelt"], newSource)
	require.Equal(t, 1, len(compileErrors))
	require.Equal(t, "Not a valid JUnit test.", compileErrors[0].(map[string]interface{})["message"])
	resp = testframework.GetAuthorized(t, officerUsername, "/java-evaluation-service/tests/"+containerId, "User-Id", officerUsername)
	require.Equal(t, http.StatusOK, resp.StatusCode)
	resultContainer = testframework.ReadSingleJsonResult(t, resp)
	require.Equal(t, newSource, resultContainer["sources"].(map[string]interface{})["HalloWelt"])
	newSource = `
	import static org.junit.Assert.assertEquals;

	import org.lecture.compiler.testframework.*;

	import org.junit.Test;

	public class HalloWelt extends AbstractTest{

		@Test
		public void itShouldWork() {
			Object target = createObject("Blubb");
			Object result = executeMethod(target,"sayBlubb");
			assertEquals("it should be blubb","blubb", (String)result);
		}
	}`
	compileErrors = patchTestSource(t, officerUsername, containerId, "HalloWelt", resultContainer["sources"].(map[string]interface{})["HalloWelt"].(string), newSource)
	require.Empty(t, compileErrors)
	compileErrors = patchTestSource(t, officerUsername, containerId, "HalloWelt", newSource, "")
	require.Nil(t, compileErrors)

	u := url.URL{Scheme: "ws", Host: testframework.GetHost(), Path: "/java-evaluation-service/user-compiler"}
	headers := make(http.Header)
	headers.Add("User-Id", userUsername)
	headers.Add("Task-Id", taskId)
	c, _, err := websocket.DefaultDialer.Dial(u.String(), headers)
	if err != nil {
		log.Fatal("dial:", err)
	}
	defer c.Close()

	codeContainer := make(map[string]interface{})
	require.Nil(t, c.ReadJSON(&codeContainer))
	containerId = codeContainer["id"].(string)
	oldUserSource := ""
	newUserSource := `
	public class Hugo {
		public static void main(String[] args) {
			System.out.println("Hugo!");
		}
	}
	`
	compileErrors = patchUserSource(t, c, "Hugo", oldUserSource, newUserSource)
	require.Len(t, compileErrors, 0)
	newUserSource = `
	public class Blubb {
		public String sayBlubb() {
			return "blubb";
		}
	}
	`
	compileErrors = patchUserSource(t, c, "Blubb", oldUserSource, newUserSource)

	require.Len(t, compileErrors, 0)
	time.Sleep(1 * time.Second)
	resp = testframework.GetAuthorized(t, officerUsername, "/java-evaluation-service/codesubmissions/"+containerId+"/test-report")
	require.Equal(t, 200, resp.StatusCode)
}

func patchTestSource(t *testing.T, username, containerId, fileName, oldSource, newSource string) []interface{} {
	patchJson := createPatchJson(t, fileName, oldSource, newSource)
	resp := testframework.PatchAuthorized(t, "/java-evaluation-service/tests/"+containerId, patchJson)
	if http.StatusOK != resp.StatusCode {
		require.Equal(t, http.StatusNoContent, resp.StatusCode)
		return nil
	}
	report := testframework.ReadSingleJsonResult(t, resp)
	return report["errors"].([]interface{})
}

func patchUserSource(t *testing.T, c *websocket.Conn, fileName, oldSource, newSource string) []interface{} {
	patchJson := createPatchJson(t, fileName, oldSource, newSource)
	err := c.WriteMessage(websocket.TextMessage, []byte(patchJson))
	require.Nil(t, err)
	result := make(map[string]interface{})
	err = c.ReadJSON(&result)
	require.Nil(t, err)
	return result["errors"].([]interface{})
}

func createPatchJson(t *testing.T, fileName, oldSource, newSource string) string {
	patch := createPatch(oldSource, newSource)
	filePatch := []map[string]interface{}{
		map[string]interface{}{
			"fileName": fileName,
			"content":  patch,
		},
	}
	return testframework.MapArrayToJsonString(t, filePatch)
}

type compilationReport struct {
	compilationErrors   []map[string]interface{} `json:"errors"`
	compilationWarnings []map[string]interface{} `json:"warnings"`
	date                string                   `json"date"`
}

func createPatch(source1, source2 string) string {
	dmp := diffmatchpatch.New()
	patches := dmp.PatchMake(source1, source2)
	return dmp.PatchToText(patches)
}

func addTestContainer(t *testing.T, username, taskId string, sources map[string]string) string {
	container := map[string]interface{}{
		"sources": sources,
		"active":  true,
		"taskId":  taskId,
	}
	resp := testframework.PostAuthorized(t, username, "/java-evaluation-service/tests", testframework.MapToJsonString(t, container), "User-Id", username)
	require.Equal(t, http.StatusCreated, resp.StatusCode)
	return resp.Header.Get("Location")
}
