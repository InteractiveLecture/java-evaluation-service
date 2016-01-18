var should = require('chai').should(),
    uuid = require('node-uuid'),
    http = require('http');

describe('test', function(){
  it('should work', function(){

    true.should.be.true;
  })
});


function createUser(username) {
  return {
    username: username,
    password: username,
    id: uuid.v4();
  };
}
