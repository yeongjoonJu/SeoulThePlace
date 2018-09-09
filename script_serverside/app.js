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
    res.json([{result: 'false', msg: err}]);
	  console.log(err);
	} else {
	  res.json([{ result: 'true' }]);
	  console.log('register succeed!');
	}
	});
});

//로그인
app.post('/login', function(req, res) {
  var id = req.body.Id;
  var password = req.body.Password;

  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, result) {
  if(err) {
    console.log('err: ' + err);
  } else {
    if(result.length === 0) {
      res.json([ {success: 'false', msg: '해당 유저가 존재하지 않습니다.'} ]);
    } else {
      if(password != result[0].password) {
        res.json([ {success: 'false', msg: '비밀번호가 일치하지 않습니다.'} ]);
      } else {
        res.json([ {success: 'true' }]);
      }
    }
  }
  });
});

//회원 정보 가져오기
app.post('/user/info', function(req,res) {
  var id = req.body.Id;
  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
      res.send(err);
    } else {
      res.json([ {Id: rows[0].Id}, {Name: rows[0].Name}, {Age: rows[0].Age},
      {Gender: rows[0].Gender}, {Type: rows[0].Type}, {FavoriteCourse: rows[0].FavoriteCourse},
      {FavoritePlace: rows[0].FavoritePlace}, {EdittedCourse: rows[0].EdittedCourse},
      {EdittedPlace: rows[0].EdittedPlace} ]);
    }
  });
});

//COURSE 정보 가져오기
app.post('/course/info', function(req, res) {
  var code = req.body.Code;
  con.query('SELECT * FROM COURSE WHERE Code = ?', code, function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
      res.send(err);
    } else {
      res.json([ {Code: rows[0].Code}, {Tag: rows[0].Tag}, {Type: rows[0].Type}, {Likes: rows[0].Likes},
      {Description: rows[0].Description}, {Details: rows[0].Details}, {PlaceCode1: rows[0].PlaceCode1},
      {PlaceCode2: rows[0].PlaceCode2}, {PlaceCode3: rows[0].PlaceCode3}, {PlaceCode4: rows[0].PlaceCode4},
      {PlaceCode5: rows[0].PlaceCode5} ]);
    }
  });
});

//PLACE 정보 가져오기
app.post('/place/info', function(req, res) {
  var code = req.body.Code;
  con.query('SELECT * FROM PLACE WHERE Code = ?', code, function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
      res.send(err);
    } else {
      res.json([ {Code: rows[0].Code}, {Name: rows[0].Name}, {Location: rows[0].Location}, {Description: rows[0].Description},
      {Details: rows[0].Details}, {Type: rows[0].Type}, {Likes: rows[0].Likes}, {Phone: rows[0].Phone}, {Image1: rows[0].Image1},
      {Image2: rows[0].Image2}, {Image3: rows[0].Image3}, {BusinessHours: rows[0].BusinessHours}, {Fee: rows[0].Fee},
      {Tag: rows[0].Tag}, {Tip: rows[0].Tip} ]);
    }
  })
});

app.post('/member', function(req, res) {
	res.send('테스트용');
	console.log('테스트용');
});

app.listen(9000, function(){
	console.log('Connected 9000 port!');
});
