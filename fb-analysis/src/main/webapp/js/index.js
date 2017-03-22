var app = new Vue({
		el : '#app-main',
		data : {
			viewModel : {
				view: ''
			},
			userInfo : {
				user : 'ddout',
				pwd : 'qq123456',
				auth : false,
				authMsg : ''
			},
			sysInfo : {
				msg:'',
				sysInfo : {
					"system_id": "-",
		            "initTime": "-",
		            "initTimeStr": "-",
		            "init_country": "-",
		            "init_SeasonAndTeam": "-",
		            "init_Games": "-"
				},
				contrys : [],
				seasonCount : 0,
				teamCount : 0,
				matchCount : 0,
				match_end_Count : 0,
				match_ne_end_Count : 0
			},
			seasonInfo : {
				region :'',
				matchName :'',
				leagueName :'',
				seasons :[]
			},
		},
		computed : {
			getModelShow : function(){
				if(this.userInfo.auth==true){
					return this.viewModel.view;
				} else {
					return false;
				}
			}
		},
		methods : {
			authUserShow : function() {
				$('#auth-Modal').modal('show');
				this.userInfo.authMsg = '';
			},
			authUser:function(event){
				var userInfoObj = this.userInfo;
				$(event.target).button('loading');
				var data = {
						user:userInfoObj.user,
						pwd:userInfoObj.pwd
				};
				$.getJSON('view/auth.do',data,function(res){
					$(event.target).button('reset');
					if(res['result'] == 'SUCCESS'){
						userInfoObj.auth = true;
						userInfoObj.pwd = '';
						$('#auth-Modal').modal('hide');
						app.loadInfo();
					} else {
						userInfoObj.auth = false;
						userInfoObj.authMsg = res['msg'];
					}
				})
			},
			loadInfo:function(event){
				var sysInfoObj = this.sysInfo;
				this.viewModel.view = 'league';
				sysInfoObj.msg = '';
				if(event){
					$(event.target).button('loading');
				}
				$.getJSON('view/viewServerInfo.do',function(res){
					if(res['result'] == 'SUCCESS'){
						sysInfoObj.sysInfo["system_id"] = res['rows']['sysInfo']['system_id'];
						sysInfoObj.sysInfo["initTime"] = res['rows']['sysInfo']['initTime'];
						sysInfoObj.sysInfo["initTimeStr"] = res['rows']['sysInfo']['initTimeStr'];
						sysInfoObj.sysInfo["init_country"] = res['rows']['sysInfo']['init_country'];
						sysInfoObj.sysInfo["init_SeasonAndTeam"] = res['rows']['sysInfo']['init_SeasonAndTeam'];
						sysInfoObj.sysInfo["init_Games"] = res['rows']['sysInfo']['init_Games'];
						//
						sysInfoObj.contrys = res['rows']['contrys'];
						sysInfoObj.seasonCount = res['rows']['seasonCount'];
						sysInfoObj.teamCount = res['rows']['teamCount'];
						sysInfoObj.matchCount = res['rows']['matchCount'];
						sysInfoObj.match_end_Count = res['rows']['match_end_Count'];
						sysInfoObj.match_ne_end_Count = res['rows']['match_ne_end_Count'];
					} else {
						sysInfoObj.msg = res['msg'];
					}
					if(event){
						$(event.target).button('reset');
					}
				});
			},
			showSeasonModel:function(country, league){
				var viewModel = this.viewModel;
				var seasonInfo = this.seasonInfo;
				var sysInfoObj = this.sysInfo;
				var data = {
						region:country.region,
						matchName:country.matchName,
						leagueName:league.leagueName
				};
				$.ajax({
					url : 'view/viewSeasonInfo.do',
					data : data,
					type : 'post',
					dataType :'json',
					success:function(res){
						if(res['result'] == 'SUCCESS'){
							viewModel.view = 'season';
							seasonInfo.seasons = res['rows'];
							seasonInfo.region = country.region;
							seasonInfo.matchName = country.matchName;
							seasonInfo.leagueName = league.leagueName;
						} else {
							sysInfoObj.msg = res['msg'];
						}
					},
					error:function(res){
						sysInfoObj.msg = res['msg'];
					}
				});
				
				
			}
		}
	});

