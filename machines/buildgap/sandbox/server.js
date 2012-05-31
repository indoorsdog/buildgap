var PI = require('./PI');
var http = require('http');

http.createServer(function (req, res) {
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end(PI.area(6));
}).listen(1337);
console.log('Server running at http://127.0.0.1:1337/');