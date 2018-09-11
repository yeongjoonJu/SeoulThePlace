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
	database : '',
  multipleStatements: true
});

//회원가입
app.post('/user/register', function(req, res) {
	var id = req.body.Id;
	var password = req.body.Password;
	var name = req.body.Name;
	var favorite_course = 'COURSEdefault';
	var favorite_place = 'default';
	var editted_course = 'editted';
	var editted_place = 'editted';

	var insertQuery = 'INSERT INTO USER VALUES(?, ?, ?, ?, ?, ?, ?)'
	var params = [id, password, name, favorite_course, favorite_place, editted_course, editted_place];

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

  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, rows) {
  if(err) {
    console.log('err: ' + err);
  } else {
    if(rows.length === 0) {
      res.json([ {success: 'false', msg: '해당 유저가 존재하지 않습니다.'} ]);
    } else {
      if(password != rows[0].password) {
        res.json([ {success: 'false', msg: '비밀번호가 일치하지 않습니다.'} ]);
      } else {
        sendUserInfo(res, rows);
      }
    }
  }
  });
});

//SNS 로그인시에 이미 있는 이메일이면 거기로 로그인
app.post('/login/bysns', function(req, res) {
  var id = req.body.Id;

  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, rows) {
  if(err) {
    console.log('err: ' + err);
  } else {
    if(rows.length === 0) {
      res.json([ {success: 'false', msg: '해당 유저가 존재하지 않습니다.'} ]);
    } else {
        sendUserInfo(res, rows);
    }
  }
  });
});

//SNS 로그인시에 이메일 권한 안줬을 경우 이름으로 로그인
app.post('/login/byname', function(req, res) {
  var name = req.body.Name;

  con.query('SELECT * FROM USER WHERE Name = ?', name, function(err, rows) {
  if(err) {
    console.log('err: ' + err);
  } else {
    if(rows.length === 0) {
      res.json([ {success: 'false', msg: '해당 유저가 존재하지 않습니다.'} ]);
    } else {
        sendUserInfo(res, rows);
    }
  }
  });
});

//id 중복체크
app.post('user/register/id_duplicatecheck', function(req, res) {
  var id = req.body.Id;

  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, result) {
    if(err) {
      console.log(err);
    } else {
      if(result.length === 0) {
        res.json([ {success: 'true'} ]);
      } else {
        res.json([ {success: 'false', msg: '중복'} ]);
      }
    }
  });
});

//name 중복체크
app.post('user/register/name_duplicatecheck', function(req, res) {
  var name = req.body.Name;

  con.query('SELECT * FROM USER WHERE Name = ?', name, function(err, result) {
    if(err) {
      console.log(err);
    } else {
      if(result.length === 0) {
        res.json([ {success: 'true'} ]);
      } else {
        res.json([ {success: 'false', msg: '중복'}]);
      }
    }
  });
});

//회원 정보 가져오기
app.post('/user/info', function(req, res) {
  var id = req.body.Id;
  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
      res.send(err);
    } else {
      sendUserInfo(res, rows);
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
      sendCourseInfo(res, rows);
    }
  });
});

//main에서 코스 리스트로 띄울 떄
app.post('/main/course_info', function(req, res) {
  var courseType = req.body.Type; //코스 타입
  var userID = req.body.Id; //로그인한 아이디
  //해당 타입에 맞는 코스 가져옴
  con.query('SELECT * FROM COURSE WHERE Type = ?', courseType, function(err, rows, fields) {
    if(err) {
      console.log('err: ' + err);
    } else {
      if(rows.length === 0) {
        res.json([ {User_Likes: 'false', Name: null, Description: null, Likes: null, location: null} ]);
      } else {
        mainCourseListInfo(res, rows, userID);
      }
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
      sendPlaceInfo(res, rows);
    }
  })
});

function mainCourseListInfo(res, rows, userID) { //rows는 해당 Type의 코스들
  //json 오브젝트 타입의 결과값들
  var jsonResult = new Object();
  //json 오브젝트 타입의 배열 형태
  var jsonArray = new Array();

  //COURSE 마다의 이름, 설명, 좋아요 수, 위치(플레이스1)
  for(var i = 0; i < rows.length; i++) {
    con.query('SELECT COURSE.Name, COURSE.Description, COURSE.Likes, PLACE.Location FROM COURSE, PLACE WHERE COURSE.Code=? AND PLACE.Code=?',
               rows[i].Code, rows[i].PlaceCode1, function(err, row, fields) {
                 if(err) {
                   console.log('err: ' + err);
                   return;
                 } else {
                   jsonResult.Name = row[0].Name;
                   jsonResult.Description = row[0].Description;
                   jsonResult.Likes = row[0].Likes;
                   jsonResult.Location = row[0].Location;
                   jsonResult.User_Likes = isCourseLiked(rows[i].Code, userID);
                   jsonArray.push(jsonResult);
                 }
               });
  }
  res.json(jsonArray);
}

//user가 해당 코스 좋아요 눌렀는지 여부
function isCourseLiked(courseID, userID) {
  con.query('SELECT * FROM COURSELIKE WHERE CourseCode = ? AND Person = ?', courseID, userID, function(err, result) {
    if(err) {
      console.log('err: ' + err);
    } else {
      if(result.length === 0) {
        return 'false';
      } else {
        return 'true';
      }
    }
  });
}

function sendUserInfo(res, rows) {
  res.json([ {Id: rows[0].Id, Name: rows[0].Name, FavoriteCourse: rows[0].FavoriteCourse,
  FavoritePlace: rows[0].FavoritePlace, EdittedCourse: rows[0].EdittedCourse, EdittedPlace: rows[0].EdittedPlace} ]);
}

function sendPlaceInfo(res, rows) {
  res.json([ {Code: rows[0].Code, Name: rows[0].Name, location: rows[0].Location, Description: rows[0].Description,
  Details: rows[0].Details, Type: rows[0].Type, Likes: rows[0].Likes, Phone: rows[0].Phone, Image1: rows[0].Image1,
  Image2: rows[0].Image2, Image3: rows[0].Image3, BusinessHours: rows[0].BusinessHours, Fee: rows[0].Fee,
  Tag: rows[0].Tag, Tip: rows[0].Tip} ]);
}

function sendCourseInfo(res, rows) {
  res.json([ {Code: rows[0].Code, Tag: rows[0].Tag, Type: rows[0].Type, Likes: rows[0].Likes,
  Description: rows[0].Description, Details: rows[0].Details, PlaceCode1: rows[0].PlaceCode1,
  PlaceCode2: rows[0].PlaceCode2, PlaceCode3: rows[0].PlaceCode3, PlaceCode4: rows[0].PlaceCode4,
  PlaceCode5: rows[0].PlaceCode5} ]);
}

app.post('/member', function(req, res) {
	res.send('테스트용');
	console.log('테스트용');
});

app.listen(9000, function(){
	console.log('Connected 9000 port!');
});
