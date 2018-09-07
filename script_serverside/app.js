var express = require('express');
var bodyParser = require('body-parser');
var mysql = require('mysql');
var app = express();

// port setup
app.set('port', process.env.PORT || 9000);

app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

var con = mysql.createConnection({
	host     : '',
	user     : '',
	password : '',
	database : ''
});

//회원가입
app.post('/user/register', function(req, res) {
	console.log('Request has come.');
	var id = req.body.Id;
	var password = req.body.Password;
	var name = req.body.Name;
	var age = req.body.Age;
	var gender = req.body.Gender;
	var type = req.body.Type;
	var favorite_course = 'COURSEdefault';
	var favorite_place = 'default';
	var editted_course = 'editted';
	var editted_place = 'editted';

	var insertQuery = 'INSERT INTO USER VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'
	var params = [id, password, name, age, gender, type, favorite_course,
	favorite_place, editted_course, editted_place];

	con.query(insertQuery, params, function(err, rows, fields) {
	if(err) {
	  res.json({ result: false, msg: err });
	  console.log(err);
	} else {
	  res.json({ result: true });
	  console.log('register succeed!');
	}
	});
});

//중복체크
app.post('/login/duplicatecheck', function(req, res) {
	var id = req.body.Id;
	var password = req.body.Password;

	con.query('SELECT Id, Password FROM USER WHERE Id = ?', id, function(err, result) {
	if(err) {
	  console.log('err : ' + err);
	} else {
	  if(result.length === 0) {
		res.json({ success: false, msg: '해당 유저가 존재하지 않습니다.' });
	  } else {
		if(password != result[0].password) {
		res.json({ success: false, msg: '비밀번호가 일치하지 않습니다.' });
	    } else {
	      res.json({ success: true });
	      }
	  }
	}
  });
});

app.post('/member', function(req, res) {
	res.send('테스트용');
	console.log('테스트용');
});

app.listen(9000, function(){
	console.log('Connected 9000 port!');
});
