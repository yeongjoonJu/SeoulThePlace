var express = require('express');
var bodyParser = require('body-parser');
var mysql = require('mysql');
var sync = require('synchronize');
var app = express();

// port setup
app.set('port', process.env.PORT || 9000);

app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

var con = mysql.createConnection({
	host              : '',
	user              : '',
	password          : '',
	database          : '',
	multipleStatements: true
});

//결과값 담을 json 타입 객체
var jsonResult = new Object();
//json 오브젝트 타입의 배열 형태
var jsonArray = new Array();
//플레이스 코드 배열
var place_code = new Array();
//코스 코드 배열
var course_code = new Array();

//회원가입
app.post('/user/register', function(req, res) {
console.log('회원가입 라우터');
	var id = req.body.Id;
	var password = req.body.Password;
	var name = req.body.Name;
	var favorite_course = 'COURSEdefault';
	var favorite_place = 'PLACEdefault';
	var editted_course = 'editted';
	var editted_place = 'editted';

	var insertQuery = 'INSERT INTO USER VALUES(?, ?, ?, ?, ?, ?, ?)'
	var params = [id, password, name, favorite_course, favorite_place,
	             editted_course, editted_place];

	con.query(insertQuery, params, function(err, rows, fields) {
	if(err) {
    	  res.json([{result: 'false', msg: err}]);
	  console.log(err);
	} else {
	  res.json([{ result: 'true' }]);
	}
	});
});

//로그인
app.post('/login', function(req, res) {
  var id = req.body.Id;
  var password = req.body.Password;
console.log('로그인 들어옴 Id : ' + id);
  con.query('SELECT * FROM USER WHERE Id = ?', id, function(err, rows) {
  if(err) {
    console.log('err: ' + err);
  } else {
    if(rows.length === 0) {
      res.json([ {success: 'false', msg: '해당 유저가 존재하지 않습니다.'} ]);
    } else {
      if(password != rows[0].Password) {
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
console.log('SNS 로그인 들어옴 Id : ' + id);
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

//id 중복체크
app.post('/user/register/id_duplicatecheck', function(req, res) {
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

//회원 정보 가져오기
app.post('/user/info', function(req, res) {
  var id = req.body.Id;
console.log('\n회원 정보 가져오기 라우터 : ' + id);
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
console.log('\n코스 정보 가져오기 라우터 : ' + code);
  con.query('SELECT * FROM COURSE WHERE Code = ?', code, function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
      res.send(err);
    } else {
      sendCourseInfo(res, rows);
    }
  });
});

//Main에서 type으로 코스 리스트로 띄울 때 필요한 정보 가져오기
app.post('/main/course_info', function(req, res) {
  var courseType = req.body.Type; //코스 타입
  var userID = req.body.Id; //로그인한 아이디
	var selectQuery = 'SELECT * FROM COURSE WHERE Type LIKE ?';
console.log('\n메인에서 type으로 코스 리스트 띄울 때 필요한 정보 라우터 : ' + courseType);

	con.query(selectQuery, ["%" + courseType + "%"], function(err, rows, fields) {
		if(err) {
			console.log('메인에서 코스 리스트err: ' + err);
		} else {
			if(rows.length === 0) {
				res.json(null);
				console.log('결과 x');
			} else {
				console.log();
				console.log('타입으로 코스 리스트 가져오는거 rows 크기 : ' + rows.length);
				res.json(getInfoToArray(rows, 'COURSE'));
			}
		}
	});
});

//Main에서 type으로 플레이스 리스트로 띄울 때 필요한 정보 가져오기
app.post('/main/place_info', function(req, res) {
	var placeType = req.body.Type;
	var userID = req.body.Id;
	var selectQuery = 'SELECT * FROM PLACE WHERE Type LIKE ?';
console.log('\n메인에서 type으로 플레이스 리스트 띄울 때 필요한 정보 라우터 : ' + placeType);

	con.query(selectQuery, ["%" + placeType + "%"], function(err, rows, fields) {
		if(err) {
			console.log('메인에서 플레이스 리스트 err : ' + err);
		} else {
			if(rows.length === 0) {
				res.json(null);
			} else {
				res.json(getInfoToArray(rows, 'PLACE'));
			}
		}
	});
});

//Main에서 해당 코스 눌렀을 때 필요한 정보 가져오기
app.post('/main/course_pushed', function(req, res) {
	var courseCode = req.body.Code;
console.log('\n메인에서 해당 코스 눌렀을 때 라우터 : ' + courseCode);
	con.query('SELECT * FROM COURSE WHERE Code = ?', courseCode, function(err, rows, fields) {
		if(err) {
			console.log('main에서 해당 코스눌렀을 때 err: ' + err)
		} else {
			pushedCourseListInfo(res,rows);
		}
	});
});

//플레이스 검색
app.post('/search/place', function(req, res) {
   var keyword = req.body.keyword;
console.log('\n플레이스 검색 키워드 : ' + keyword);
   con.query('SELECT * FROM PLACE WHERE Name LIKE ? OR Location LIKE ? OR Type LIKE ?',
 ["%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%"], function(err, rows, fields) {
     if(err) {
       console.log('라우터err : ' + err);
     } else {
       if(rows.length === 0) {
         res.json(null);
       } else {
	 res.json(getInfoToArray(rows, 'PLACE'));
       }
     }
   });
});

//코스 검색
app.post('/search/course', function(req, res) {
  var keyword = req.body.keyword;
console.log('\n코스 검색 키워드 : ' + keyword);
  con.query('SELECT * FROM COURSE WHERE Name LIKE ? OR Type LIKE ?',
["%"+keyword+"%", "%"+keyword+"%"], function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
    } else {
      if(rows.length === 0) {
        res.json(null);
      } else {
	res.json(getInfoToArray(rows, 'COURSE'));
      }
    }
  });
});

//PLACE 정보 가져오기
app.post('/place/info', function(req, res) {
  var code = req.body.Code;
console.log('\n플레이스 정보 가져오기 : ' + code);
  con.query('SELECT * FROM PLACE WHERE Code = ?', code, function(err, rows, fields) {
    if(err) {
      console.log('err : ' + err);
      res.send(err);
    } else {
      sendPlaceInfo(res, rows);
    }
  });
});

//코스 좋아요
app.post('/course/like', function(req, res) {
  var courseCode = req.body.Code;
  var user_ID = req.body.Id;
  var courseLikes; //해당 코스의 좋아요 수
  var courseLiked; //해당 코스 좋아요 여부

  sync.fiber(function() {
    var result = sync.await(con.query('SELECT Likes FROM COURSE WHERE Code = ?', courseCode, sync.defer()));
    courseLikes = result[0].Likes;
    courseLiked = isCourseLiked(courseCode, user_ID);
    if(courseLiked == 'true') {
      if(courseLikes > 0) {
        courseLikes = courseLikes - 1;
      }
      res.json([ {isCourseLiked: 'true', Likes: courseLikes} ]);
      con.query('DELETE FROM COURSELIKE WHERE CourseCode = ? AND Person = ?', [courseCode, user_ID]);
      con.query('UPDATE COURSE set Likes = ? WHERE Code = ?', [courseLikes, courseCode]);
    } else {
       courseLikes = courseLikes + 1;
       res.json([ {isCourseLiked: 'false', Likes: courseLikes} ]);
       con.query('INSERT INTO COURSELIKE VALUES(?, ?)', [courseCode, user_ID]);
       con.query('UPDATE COURSE set Likes = ? WHERE Code = ?', [courseLikes, courseCode]);
      }
  });
});

//플레이스 좋아요
app.post('/place/like', function(req, res) {
  var placeCode = req.body.Code;
  var user_ID = req.body.Id;
  var placeLikes; //해당 플레이스 좋아요 수
  var placeLiked; //해당 플레이스 좋아요 여부

  sync.fiber(function() {
    var result = sync.await(con.query('SELECT Likes FROM PLACE WHERE Code = ?', placeCode, sync.defer()));
    placeLikes = result[0].Likes;
    placeLiked = isPlaceLiked(placeCode, user_ID);
    if(placeLiked == 'true') {
      if(placeLikes > 0) {
        placeLikes = placeLikes - 1;
      }
      res.json([ {isPlaceLiked: 'true', Likes: placeLikes} ]);
      con.query('DELETE FROM PLACELIKE WHERE PlaceCode = ? AND Person = ?', [placeCode, user_ID]);
      con.query('UPDATE PLACE set Likes = ? WHERE Code = ?', [placeLikes, placeCode]);
    } else {
       placeLikes = placeLikes + 1;
       res.json([ {isPlaceLiked: 'false', Likes: placeLikes} ]);
       con.query('INSERT INTO PLACELIKE VALUES(?, ?)', [placeCode, user_ID]);
       con.query('UPDATE PLACE set Likes = ? WHERE Code = ?', [placeLikes, placeCode]);
      }
  });
});

//코스 좋아요 상태
app.post('/course/like/status', function(req, res) {
	var courseCode = req.body.Code;
	var user_ID = req.body.Id;

	sync.fiber(function() {
       	  var isCourseLike = isCourseLiked(courseCode, user_ID);
	  res.json([ {isCourseLiked: isCourseLike} ]);
        });
});

//플레이스 좋아요 상태
app.post('/place/like/status', function(req, res) {
	var placeCode = req.body.Code;
	var user_ID = req.body.Id;

	sync.fiber(function() {
	  var isPlaceLike = isPlaceLiked(placeCode, user_ID);
	  res.json([ {isPlaceLiked: isPlaceLike} ]);
	});
});

//코스 수정 할 때 이름과 아이디 중복체크
app.post('/course/editted/duplicatecheck', function(req, res) {
  var user_ID = req.body.Id;
  var editedCourse_name = req.body.Name;

  var selectQuery = 'SELECT Person, Name FROM EDITTEDCOURSE WHERE Person=? AND Name=?';
  var params = [user_ID, editedCourse_name];

  con.query(selectQuery, params, function(err, result) {
    if(err) {
      console.log(err);
    } else {
      if(result.length === 0) {
        res.json([ {success: 'true'} ]);
      } else {
        res.json([ {success: 'false'} ]);
      }
    }
  });
});

//코스 수정 완료 후 입력
app.post('/course/editted', function(req, res) {
  var user_ID = req.body.Id;
  var edittedCourse_name = req.body.Name;
  var edittedCourse_description = req.body.Description;
  var placeCode1 = req.body.PlaceCode1;
  var placeCode2 = req.body.PlaceCode2;
  var placeCode3 = req.body.PlaceCode3;
  var placeCode4 = req.body.PlaceCode4;
  var placeCode5 = req.body.PlaceCode5;

  var insertQuery = 'INSERT INTO EDITTEDCOURSE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)';
  var params = [null, user_ID, edittedCourse_name, edittedCourse_description, placeCode1, placeCode2,
                placeCode3, placeCode4, placeCode5];

  con.query(insertQuery, params, function(err, rows, fields) {
    if(err) {
      res.json([ {success: 'false'}]);
      console.log(err);
    } else {
      res.json( {success: 'true'});
    }
  });
});

//거리계산을 위한 모든 Place 리스트 다 받아오기
app.post('/place/all', function(req, res) {
  jsonArray = initJsonArray(jsonArray);
  var query = 'SELECT Code, Name, Image1, Coordinate_X, Coordinate_Y FROM PLACE WHERE Code NOT IN("PLACEdefault")';
  sync.fiber(function() {
    var result = sync.await(con.query(query, sync.defer()));
    for(var i = 0; i < result.length; i++) {
      jsonResult = new Object();
      jsonResult.Code = result[i].Code;
      jsonResult.Name = result[i].Name;
      jsonResult.Image1 = result[i].Image1;
      jsonResult.Coordinate_X = result[i].Coordinate_X;
      jsonResult.Coordinate_Y = result[i].Coordinate_Y;
      jsonArray.push(jsonResult);
    }
    res.json([ {jsonArr: jsonArray} ]);
  });
});

//커스텀 코스 띄울 때
app.post('/edited_course/info', function(req, res) {
	var user_ID = req.body.Id;
	var query_edittedCourse = 'SELECT * FROM EDITTEDCOURSE WHERE Person = ?';
	jsonArray = initJsonArray(jsonArray);
console.log();
console.log('커스텀 코스 띄울 때 Id : ' + user_ID);
	sync.fiber(function() {
		//edittedcourse 정보 받아오기
		var query_edittedCourse_result = sync.await(con.query(query_edittedCourse, user_ID, sync.defer()));
		for(var i = 0; i < query_edittedCourse_result.length; i++) {
			jsonResult = new Object();
			jsonResult.edittedCourse_Code = query_edittedCourse_result[i].Code;
			jsonResult.edittedCourse_Name = query_edittedCourse_result[i].Name;
			jsonResult.edittedCourse_Description = query_edittedCourse_result[i].Description;
			jsonResult.edittedCourse_PlaceCode1 = query_edittedCourse_result[i].PlaceCode1;
			jsonResult.edittedCourse_PlaceCode2 = query_edittedCourse_result[i].PlaceCode2;
			jsonResult.edittedCourse_PlaceCode3 = query_edittedCourse_result[i].PlaceCode3;
			jsonResult.edittedCourse_PlaceCode4 = query_edittedCourse_result[i].PlaceCode4;
			jsonResult.edittedCourse_PlaceCode5 = query_edittedCourse_result[i].PlaceCode5;
			jsonArray.push(jsonResult);
		}
	res.json([ {jsonArr: jsonArray} ]);
	});
});

//커스텀 코스 지우면
app.post('/edited_course/delete', function(req, res) {
	var userID = req.body.Id;
	var edittedCourse_code = req.body.Code;

	var query = 'DELETE FROM EDITTEDCOURSE WHERE Code = ?';
	con.query(query, edittedCourse_code, function(err, rows, fields) {
		if(err) {
			res.json([ {success: 'false'} ]);
			console.log(err);
		} else {
			res.json([ {success: 'true'} ]);
		}
	});
});

//좋아요 한 코스 띄우기
app.post('/likedcourses', function(req, res) {
	var userID = req.body.Id;
	var selectQuery = 'SELECT CourseCode FROM COURSELIKE WHERE Person = ?';
	var selectQuery2 = 'SELECT * FROM COURSE WHERE Code=?';
	var selectQuery3 = 'SELECT Image1 FROM PLACE WHERE Code=?'

	jsonArray = initJsonArray(jsonArray);
	sync.fiber(function() {
		var courseCodeList = sync.await(con.query(selectQuery, userID, sync.defer()));
		for(var i = 0; i < courseCodeList.length; i++) {
			var result = sync.await(con.query(selectQuery2, courseCodeList[i].CourseCode, sync.defer()));
			jsonResult = new Object();
			jsonResult.Code = result[0].Code;
			jsonResult.Name = result[0].Name;
			jsonResult.Type = result[0].Type;
			jsonResult.Likes = result[0].Likes;
			jsonResult.Details = result[0].Details;
			jsonResult.PlaceCode1 = result[0].PlaceCode1;
			jsonResult.PlaceCode2 = result[0].PlaceCode2;
			jsonResult.PlaceCode3 = result[0].PlaceCode3;
			jsonResult.PlaceCode4 = result[0].PlaceCode4;
			jsonResult.PlaceCode5 = result[0].PlaceCode5;
			var result = sync.await(con.query(selectQuery3, jsonResult.PlaceCode1, sync.defer()));
			jsonResult.Image = result[0].Image1;
			jsonArray.push(jsonResult);
		}
		res.json([ {jsonArr: jsonArray}]);
	});
});

//좋아요 한 플레이스 띄우기
app.post('/likedplaces', function(req, res) {
	var userID = req.body.Id;
	var selectQuery = 'SELECT PlaceCode FROM PLACELIKE WHERE Person = ?';
	var selectQuery2 = 'SELECT * FROM PLACE WHERE Code=?';
	jsonArray = initJsonArray(jsonArray);
	sync.fiber(function() {
		var placeCodeList = sync.await(con.query(selectQuery, userID, sync.defer()));
		for(var i = 0; i < placeCodeList.length; i++) {
			var result = sync.await(con.query(selectQuery2, placeCodeList[i].PlaceCode, sync.defer()));
			jsonResult = new Object();
			jsonResult.Code = result[0].Code;
			jsonResult.Name = result[0].Name;
			jsonResult.location = result[0].Location;
			jsonResult.Type = result[0].Type;
			jsonResult.Likes = result[0].Likes;
			jsonResult.Details = result[0].Details;
			jsonResult.Phone = result[0].Phone;
			jsonResult.Parking = result[0].Parking;
			jsonResult.Image1 = result[0].Image1;
			jsonResult.Image2 = result[0].Image2;
			jsonResult.Image3 = result[0].Image3;
			jsonResult.BusinessHours = result[0].BusinessHours;
			jsonResult.Fee = result[0].Fee;
			jsonResult.Tip = result[0].Tip;
			jsonResult.Coordinate_X = result[0].Coordinate_X;
			jsonResult.Coordinate_Y = result[0].Coordinate_Y;
			jsonArray.push(jsonResult);
		}
		res.json([ {jsonArr: jsonArray}]);
	});
});


//코스 및 플레이스 검색시 json 형태 배열에 데이터 담기.
function getInfoToArray(rows, table) {
  if(table == 'COURSE') {
    initJsonArray(jsonArray);
    for(var i = 0; i < rows.length; i++) {
      jsonResult = new Object();
      jsonResult.Code = rows[i].Code;
      jsonResult.Name = rows[i].Name;
      jsonResult.Type = rows[i].Type;
      jsonResult.Likes = rows[i].Likes;
      jsonResult.Details = rows[i].Details;
      jsonResult.PlaceCode1 = rows[i].PlaceCode1;
      jsonResult.PlaceCode2 = rows[i].PlaceCode2;
      jsonResult.PlaceCode3 = rows[i].PlaceCode3;
      jsonResult.PlaceCode4 = rows[i].PlaceCode4;
      jsonResult.PlaceCode5 = rows[i].PlaceCode5;
      jsonArray.push(jsonResult);
    }
  } else {
    initJsonArray(jsonArray);
    for(var i = 0; i < rows.length; i++) {
      jsonResult = new Object();
      jsonResult.Code = rows[i].Code;
      jsonResult.Name = rows[i].Name;
      jsonResult.location = rows[i].Location;
      jsonResult.Details = rows[i].Details;
      jsonResult.Type = rows[i].Type;
      jsonResult.Likes = rows[i].Likes;
      jsonResult.Phone = rows[i].Phone;
      jsonResult.Parking = rows[i].Parking;
      jsonResult.Image1 = rows[i].Image1;
      jsonResult.Image2 = rows[i].Image2;
      jsonResult.Image3 = rows[i].Image3;
      jsonResult.BusinessHours = rows[i].BusinessHours;
      jsonResult.Fee = rows[i].Fee;
      jsonResult.Tip = rows[i].Tip;
      jsonResult.Coordinate_X = rows[i].Coordinate_X;
      jsonResult.Coordinate_Y = rows[i].Coordinate_Y;
      jsonArray.push(jsonResult);
    }
  }
  return jsonArray;
}

function mainCourseListInfo(res, rows, userID) { //rows는 해당 Type의 코스들
  initJsonArray(jsonArray);

  //코스들의 Code 값들
  for(var i = 0; i < rows.length; i++) {
    course_code[i] = rows[i].Code;
  }

  //COURSE 마다의 코드, 이름, 설명, 좋아요 수, 위치(플레이스1)
  sync.fiber(function() {
    for(var i = 0; i < rows.length; i++) {
      var result = sync.await(con.query('SELECT COURSE.Code, COURSE.Name, COURSE.Likes, PLACE.Location, PLACE.Image1 FROM COURSE, PLACE WHERE COURSE.Code=? AND PLACE.Code=?',
                 [rows[i].Code, rows[i].PlaceCode1], sync.defer()));
                     jsonResult = new Object();
                     jsonResult.Code = result[0].Code;
                     jsonResult.Name = result[0].Name;
                     jsonResult.Likes = result[0].Likes;
                     jsonResult.location = result[0].Location;
                     jsonResult.User_Likes = isCourseLiked(course_code[i], userID);
                     jsonResult.Image = result[0].Image1;
                     jsonArray.push(jsonResult);
                 };
             res.json([ {jsonArr: jsonArray} ]);
    });
}

function pushedCourseListInfo(res, rows) {
  //rows는 해당 코스 하나
  jsonArray = initJsonArray(jsonArray);
  place_code = [];
  place_code[0] = rows[0].PlaceCode1;
  place_code[1] = rows[0].PlaceCode2;
  place_code[2] = rows[0].PlaceCode3;
  place_code[3] = rows[0].PlaceCode4;
  place_code[4] = rows[0].PlaceCode5;

  sync.fiber(function() {
    for(var i = 0; i < 5; i++) {
      var result = sync.await(con.query('SELECT Image1, Name, Location, Details, Coordinate_X, Coordinate_Y FROM PLACE WHERE Code = ?', [place_code[i]], sync.defer()));
      if(result[0] == undefined)
	break;
      jsonResult = new Object();
      jsonResult.Image1 = result[0].Image1;
      jsonResult.Name = result[0].Name;
      jsonResult.location = result[0].Location;
      jsonResult.Details = result[0].Details;
      jsonResult.Coordinate_X = result[0].Coordinate_X;
      jsonResult.Coordinate_Y = result[0].Coordinate_Y;
      jsonArray.push(jsonResult);
    }
    res.json([ {CourseDetails: rows[0].Details, jsonArr: jsonArray} ]);
  });
}

//user가 해당 코스 좋아요 눌렀는지 여부
function isCourseLiked(courseID, userID) {
  var result = sync.await(con.query('SELECT * FROM COURSELIKE WHERE CourseCode = ? AND Person = ?', [courseID, userID], sync.defer()));
  if(result.length === 0) {
    return 'false';
  } else {
    return 'true';
  }
}

//user가 해당 플레이스 좋아요 눌렀는지 여부
function isPlaceLiked(placeID, userID) {
   var result = sync.await(con.query('SELECT * FROM PLACELIKE WHERE PlaceCode = ? AND Person = ?',[placeID, userID], sync.defer()));
   if(result.length === 0) {
     return 'false';
   } else {
     return 'true';
   }
}

function initJsonArray(jsonArr) {
  if(jsonArr.length > 0) {
    jsonArr.length = 0;
  }
  return jsonArr;
}

function sendUserInfo(res, rows) {
  res.json([ {success: 'true', Id: rows[0].Id, Name: rows[0].Name, FavoriteCourse: rows[0].FavoriteCourse,
  FavoritePlace: rows[0].FavoritePlace, EdittedCourse: rows[0].EdittedCourse, EdittedPlace: rows[0].EdittedPlace} ]);
}

function sendPlaceInfo(res, rows) {
  res.json([ {Code: rows[0].Code, Name: rows[0].Name, location: rows[0].Location,
  Details: rows[0].Details, Type: rows[0].Type, Likes: rows[0].Likes, Phone: rows[0].Phone, Parking: rows[0].Parking,
  Image1: rows[0].Image1, Image2: rows[0].Image2, Image3: rows[0].Image3, BusinessHours: rows[0].BusinessHours,
  Fee: rows[0].Fee,Tip: rows[0].Tip, Coordinate_X: rows[0].Coordinate_X, Coordinate_Y: rows[0].Coordinate_Y}  ]);
}

function sendCourseInfo(res, rows) {
  res.json([ {Code: rows[0].Code, Name: rows[0].Name, Type: rows[0].Type, Likes: rows[0].Likes,
  Details: rows[0].Details, PlaceCode1: rows[0].PlaceCode1,  PlaceCode2: rows[0].PlaceCode2,
  PlaceCode3: rows[0].PlaceCode3, PlaceCode4: rows[0].PlaceCode4, PlaceCode5: rows[0].PlaceCode5} ]);
}

app.get('/member', function(req, res) {
	res.send('테스트용');
	console.log('테스트용');
});

app.listen(9000, function(){
	console.log('Connected 9000 port!');
});
