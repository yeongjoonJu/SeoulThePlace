app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

var con = mysql.createConnection({
	host     : 'seoultheplace.cyhr06cnp2gp.ap-northeast-2.rds.amazonaws.com',
	user     : 'xxxx',
	password : 'xxxx',
	database : 'seoultheplace'
});

// get 방식의 라우팅
// app.get('/user', function(req, res){
// console.log('jooyoungjoon');
// res.send('jooyoungjoon');
//
// 	con.connect();
// 	var sql = 'SELECT * FROM USER';
// 	con.query(sql, function(err, rows, fields) {
// 	if(err) {
// 		console.log(err);
// 	} else {
// 		for(var i =0; i < rows.length; i++) {
// 			console.log(rows[i].NAME);
// 			res.send(rows[i].NAME);
// 			}
// 		}
// 	});
// });

//post방식 라우터
app.post('/user', function(req, res) {
console.log(req.body.user_id);
console.log(req.body.name);
	con.connect();
	var sql = 'SELECT * FROM USER';
	con.query(sql, function(err, rows, fields) {
	if(err) {
		console.log(err);
	} else {
		for(var i = 0; i < rows.length; i++) {
			res.send(rows[i].NAME);
			}
		}
	});
	con.end();
});

app.listen(9000, function(){
	console.log('Connected 9000 port!');
});
