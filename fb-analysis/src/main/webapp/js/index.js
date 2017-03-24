var app = new Vue({
		el : '#app-main',
		data : {
			errorMsg:'',
			userInfo : {
				user : '',
				pwd : '',
				auth : false,
				authMsg : ''
			},
			sysInfo:{
				initTime:'',
				init_Games:'',
				init_SeasonAndTeam:'',
				init_country:'',
				system_id:''
			},
			sysView:{
				ThreadGroup4Games:'',
				ThreadGroup4SeasonAndTeam:'',
				country:'',
				league:'',
				matchCount:'',
				matchEnd:'',
				matchUnEnd:'',
				odds_info:'',
				season:'',
				season_team:'',
				team:''
			},
			analysis:{
				sreachType : 1,
				sreachKey : '',
				matchs:[]
			},
			matchInfo: {
				isActive:false,
				oddsActive:false,
				analysisActive:false,
				id:'',
				matchObj:{
				    "id": "",
				    "region": "",
				    "odds_info_3": "",
				    "groupName": "",
				    "leagueName": "",
				    "home_score": "",
				    "odds_info_0": "",
				    "seasonName": "",
				    "odds_info_1": "",
				    "roundName": "",
				    "away_score": "",
				    "match_result": "",
				    "boxName": "",
				    "score_result": "",
				    "season_id": "",
				    "gameTime": "",
				    "refererURI": "",
				    "match_site": "",
				    "away_team": "",
				    "infoURI": "",
				    "home_team": "",
				    "matchName": "",
				    "odds_info_URI": ""
				},
				odds:[]
			}
		},
		methods : {
			authUserShow : function() {
				$('#auth-Modal').modal('show');
				this.userInfo.authMsg = '';
			},
			authUser:function(event){
				var _this = this;
				$(event.target).button('loading');
				var data = {
						user:_this.userInfo.user,
						pwd:_this.userInfo.pwd
				};
				$.getJSON('view/auth.do',data,function(res){
					$(event.target).button('reset');
					if(res['result'] == 'SUCCESS'){
						_this.userInfo.auth = true;
						_this.userInfo.pwd = '';
						$('#auth-Modal').modal('hide');
						app.loadInfo();
					} else {
						_this.userInfo.auth = false;
						_this.userInfo.authMsg = res['msg'];
					}
				})
			},
			loadInfo:function(event){
				var _this = this;
				if(event){
					$(event.target).button('loading');
				}
				$.getJSON('view/viewServerInfo.do',function(res){
					if(res['result'] == 'SUCCESS'){
						var resData = res['rows']
						$.extend(_this.sysInfo, resData['sysInfo']);
						$.extend(_this.sysView, resData['sysView']);
					} else {
						_this.errorMsg = res['msg'];
					}
					if(event){
						$(event.target).button('reset');
					}
				});
			},
			analysisSreach:function(v){
				var _this = this;
				if(v == 'x'){
				} else if(v == '2'){
					_this.analysis.sreachType = 2;
				} else if(v == '3'){
					_this.analysis.sreachType = 3;
				} else{
					_this.analysis.sreachType = 1;
				}
				if(_this.analysis.sreachKey == ''){
					return;
				}
				$.ajax({
					url : 'view/matchSreach.do',
					data : {"sreachkey" : _this.analysis.sreachKey, "sreachType" : _this.analysis.sreachType},
					dataType:'json',
					type:'post',
					success:function(res){
						if(res['result'] == 'SUCCESS'){
							var data = res['rows'];
							_this.analysis.matchs = data;
							_this.closeMatchInfo();
						} else {
							_this.errorMsg = res['msg'];
						}
					},
					error:function(res){
						_this.errorMsg = res;
					}
				});
			},
			showMatchInfo:function(_id){
				if(_id == ''){
					return;
				}
				var _this = this;
				_this.matchInfo.id = _id;
				_this.matchInfo.isActive = true;
				$.getJSON('view/matchInfoView.do', {"matchId":_this.matchInfo.id}, function(res){
					if(res['result'] == 'SUCCESS'){
						$.extend(_this.matchInfo.matchObj, res['rows']['matchObj']);
						_this.matchInfo.matchObj.odds = res['rows']['oddsObj'];
					} else {
						_this.errorMsg = res['msg'];
					}
				});
			},
			closeMatchInfo:function(){
				this.matchInfo.isActive = false;
				this.matchInfo.oddsActive = false;
				this.matchInfo.analysisActive = false;
			},
			showOddsInfo: function(){
				if(this.matchInfo.oddsActive == true){
					this.matchInfo.oddsActive = false;
				} else {
					this.matchInfo.oddsActive = true;
				}
			},
			viewAnlysis: function(_id,event){
				if(_id == ''){
					return;
				}
				if(event){
					$(event.target).button('loading');
				}
				var _this = this;
				_this.matchInfo.analysisActive = true;
				$.getJSON('view/viewAnalysis.do', {"matchId":_id}, function(res){
					console.log(res)
					if(res['result'] == 'SUCCESS'){
						
					} else {
						_this.errorMsg = res['msg'];
					}
					if(event){
						$(event.target).button('reset');
					}
				});
			}
		}
	});

