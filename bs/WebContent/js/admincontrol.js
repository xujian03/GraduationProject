/**
 * 
 */
addUser();
addMessage();
addGrade(0);
addCollege();
addBigClass();
addSmallClass(0);
addGoods(1);
var TOTALGOODS=1;//商品列表总共有几页
var INDEXGOODS=1;//商品列表总共有几页
//向用户表中添加用户信息
function addUser(){
	$.ajax({
		url:'admin/getuser',
		success:function(msg){
			$('#userTable').dataTable().fnDestroy();
			$("#userlist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var status="<span style='color:red'>被封禁</span>";
				var btn="<button onclick='unBannedUser("+msg[i].userId+")' class='btn btn-success btn-xs'>解封</button>";
				if(msg[i].isLocked==false){
					btn="<button onclick='bannedUser("+msg[i].userId+")' class='btn btn-danger btn-xs'>封禁</button>";
					status="<span style='color:green'>正常</span>";
				}
				string+="<tr>"+
						"<td>"+msg[i].userId+"</td>"+
						"<td>"+msg[i].userName+"</td>"+
						"<td>"+msg[i].userEmail+"</td>"+
						"<td>"+msg[i].userMsg+"</td>"+
						"<td>"+msg[i].userStudentId+"</td>"+
						"<td>"+status+"</td>"+             
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#userlist").append(string);
			$('#userTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}
//封禁
function bannedUser(uid){
	$.ajax({
		url:'admin/banned',
		type:'post',
		data:{
			userId:uid
		},
		success:function(msg){
			if(msg.status!=1){alert(msg.info);return;}
			addUser();
		}
	})
}
//解封
function unBannedUser(uid){
	$.ajax({
		url:'admin/unbanned',
		type:'post',
		data:{
			userId:uid
		},
		success:function(msg){
			if(msg.status!=1){alert(msg.info);return;}
			addUser();
		}
	})
}

//删除通知
function deleteMessage(messageId){
	var isdelete=confirm("是否删除该通知?");
	if(isdelete){
		$.ajax({
			url:'admin/delete',
			type:'post',
			data:{
				messageId:messageId
			},
			success:function(){
				addMessage();
			}
			
		})
	}
}




//发送系统通知
function addMessage(searchString){
	
	$.ajax({
		url:'admin/getmessage',
		data:{
			keyword:searchString
		}, 
		success:function(msg){
			$('#messageTable').dataTable().fnDestroy();
			$("#messagelist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var status="<span style='color:orange'>已读</span>";
				var btn="<button onclick='deleteMessage("+msg[i].messageId+");' class='btn btn-success btn-xs'>删除</button>";
				if(msg[i].isRead==0){
					status="<span style='color:green'>未读</span>";
				}
				string+="<tr>"+
						"<td>"+msg[i].messageId+"</td>"+
						"<td>"+msg[i].userId+"</td>"+
						"<td>"+msg[i].messageString+"</td>"+
						"<td>"+status+"</td>"+
						"<td>"+msg[i].messageTime+"</td>"+           
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#messagelist").append(string);
			
			$('#messageTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}


$("#messagebtn").click(function(){
	var message=$("#message").val();
	$.ajax({
		url:'admin/sendmessage',
		type:'post',
		data:{
			message:message
		},
		success:function(msg){
			alert(msg.info);
			if(msg.status==1){
				addMessage();
			}
		}
	})
})

//通知信息搜索按钮
$("#messageSearchBtn").click(function(){
	var message=$("#messageSearch").val();
	addMessage(message);
})

$.ajax({
	url:'admin/getlogincount',
	success:function(msg){
//		userloginchart(msg);
		var myChart = echarts.init(document.getElementById('userloginchart'));
		var data=[];
		var date=[];
		for (var i = 0; i < msg.length; i++) {
		    data.push(msg[i].loginCount);
		    date.push(msg[i].loginDate);
		}
		
		
		option = {
			    tooltip: {
			        trigger: 'axis',
			        position: function (pt) {
			            return [pt[0], '10%'];
			        }
			    },
			    title: {
			        left: 'center',
			        text: '用户活跃日活跃度',
			    },
			    legend: {
			        top: 'bottom',
			        data:['意向']
			    },
			    toolbox: {
			        show: true,
			        feature: {
			            dataView: {show: true, readOnly: false},
			            magicType: {show: true, type: ['line', 'bar']},
			            restore: {show: true},
			            saveAsImage: {show: true}
			        }
			    },
			    xAxis: {
			        type: 'category',
			        name:'日期',
			        boundaryGap: false,
			        data: date
			    },
			    yAxis: {
			        type: 'value',
			        name:'登陆次数',
			        boundaryGap: [0, '100%']
			    },
			    dataZoom: [{
			        type: 'slider',
			        start: 10,
			        end: 40
			    }],
			    series: [
			        {
			            name:'用户活跃度',
			            type:'line',
			            smooth:true,
			            symbol: 'none',
			            sampling: 'average',
			            itemStyle: {
			                normal: {
			                    color: 'rgb(255, 70, 131)'
			                }
			            },
			            areaStyle: {
			                normal: {
			                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
			                        offset: 0,
			                        color: 'rgb(255, 158, 68)'
			                    }, {
			                        offset: 1,
			                        color: 'rgb(255, 70, 131)'
			                    }])
			                }
			            },
			            data: data
			        }
			    ]
			};

		myChart.setOption(option);
	}
})


//绑定添加学院按钮
$("#addschoolbtn").click(function(){
	var schoolname=$("#school").val();
	$.ajax({
		url:'admin/addschool',
		type:'post',
		data:{
			college:schoolname
		},
		success:function(msg){
			if(msg.status==1){
				alert(msg.info);
				window.location.reload();
			}
		}
	})
})

//添加班级
$("#addgradebtn").click(function(){
	var schoolname=$("#xueyuan").val();
	var grade=$("#gclass").val();
	$.ajax({
		url:'admin/addgrade',
		type:'post',
		data:{
			college:schoolname,
			grade:grade
		},
		success:function(msg){
			alert(msg.info);
			if(msg.status==1){
				window.location.reload();
			}
		}
	})
})

//获取select中的学院
$.ajax({
	url:'user/getcollege',
	success:function(msg){
		$("#xueyuan").append(msg);
	}
})
//大类别中添加ooptiobn
$.ajax({
	url:'/bs/goods/getbigclass',
	success:function(msg){
		console.log(msg);
		$("#bigClass").append(msg);
	}
})


//向学院表中添加学院信息
function addCollege(){
	$.ajax({
		url:'admin/getcollege',
		success:function(msg){
			$('#collegeTable').dataTable().fnDestroy();
			$("#collegelist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var btn="<button onclick='deleteCollege("+msg[i].classCollegeId+")' class='btn btn-danger btn-xs'>删除</button>"+
				"&nbsp;<button onclick='addGrade("+msg[i].classCollegeId+")' class='btn btn-success btn-xs'>查看本学院的班级</button>";

				string+="<tr>"+
						"<td>"+msg[i].classCollegeId+"</td>"+
						"<td>"+msg[i].classCollegeName+"</td>"+            
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#collegelist").append(string);
			$('#collegeTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}
//删除某个学院
function deleteCollege(classCollegeId){
	var isDelete=confirm("是否删除学院编号为"+classCollegeId+"的学院");
	if(isDelete){
		$.ajax({
			url:'admin/deletecollage',
			type:'post',
			data:{
				classCollegeId:classCollegeId
			},
			success:function(msg){
				alert(msg.info);
				addCollege();
				addGrade();
			}
		})
	}
}

//向班级表中添加班级信息
function addGrade(collegeId){
	$.ajax({
		url:'admin/getgrade',
		data:{
			collegeId:collegeId
		},
		success:function(msg){
			$('#gradeTable').dataTable().fnDestroy();
			$("#gradelist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var btn="<button onclick='deleteGrade("+msg[i].classGradeId+")' class='btn btn-danger btn-xs'>删除</button>";

				string+="<tr>"+
						"<td>"+msg[i].classGradeId+"</td>"+
						"<td>"+msg[i].classGradeName+"</td>"+            
						"<td>"+msg[i].classCollegeId+"</td>"+            
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#gradelist").append(string);
			$('#gradeTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}


//删除某个班级
function deleteGrade(classGradeId){
	var isDelete=confirm("是否删除班级编号为"+classGradeId+"的班级");
	if(isDelete){
		$.ajax({
			url:'admin/deletegrade',
			type:'post',
			data:{
				classGradeId:classGradeId
			},
			success:function(msg){
				alert(msg.info);
				addGrade();
			}
		})
	}
}


//向大类别表中添加学院信息
function addBigClass(){
	$.ajax({
		url:'admin/getbigclass',
		success:function(msg){
			$('#bigClassTable').dataTable().fnDestroy();
			$("#bigClasslist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var btn="<button onclick='deleteBigClass("+msg[i].bigClassId+")' class='btn btn-danger btn-xs'>删除</button>"+
				"&nbsp;<button onclick='addSmallClass("+msg[i].bigClassId+")' class='btn btn-success btn-xs'>查看该类别包含的小类别</button>";

				string+="<tr>"+
						"<td>"+msg[i].bigClassId+"</td>"+
						"<td>"+msg[i].bigClass+"</td>"+            
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#bigClasslist").append(string);
			$('#bigClassTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}
//向小类别表中添加班级信息
function addSmallClass(bigClassId){
	$.ajax({
		url:'admin/getsmallclass',
		data:{
			bigClassId:bigClassId
		},
		success:function(msg){
			$('#smallClassTable').dataTable().fnDestroy();
			$("#smallClasslist").empty();
			var string="";
			for(var i=0;i<msg.length;i++)
			{
				var btn="<button onclick='deleteSmallClass("+msg[i].smallClassId+")' class='btn btn-danger btn-xs'>删除</button>";

				string+="<tr>"+
						"<td>"+msg[i].smallClassId+"</td>"+
						"<td>"+msg[i].smallClass+"</td>"+            
						"<td>"+msg[i].bigClassId+"</td>"+            
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#smallClasslist").append(string);
			$('#smallClassTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
		}
	})
}
//绑定大类别添加
$("#bigclassbtn").click(function(){
	var bigclass=$("#bigclassinput").val();
	$.ajax({
		url:'admin/addbigclass',
		type:'post',
		data:{
			bigclass:bigclass
		},
		success:function(msg){
			if(msg.status==1){
				alert(msg.info);
				window.location.reload();
			}
		}
	})
})

//添加小类别
$("#addsmallclassbtn").click(function(){
	var bigClass=$("#bigClass").val();
	var smallclassinput=$("#smallclassinput").val();
	$.ajax({
		url:'admin/addsmallclass',
		type:'post',
		data:{
			bigClass:bigClass,
			smallclass:smallclassinput
		},
		success:function(msg){
			alert(msg.info);
			if(msg.status==1){
				window.location.reload();
			}
		}
	})
})


//删除大类别
function deleteBigClass(classCollegeId){
	var isDelete=confirm("是否删除类别编号为"+classCollegeId+"的大类别");
	if(isDelete){
		$.ajax({
			url:'admin/deletebigclass',
			type:'post',
			data:{
				bigClassId:classCollegeId
			},
			success:function(msg){
				alert(msg.info);
				window.location.reload();
			}
		})
	}
}

//删除某个班级
function deleteSmallClass(classGradeId){
	var isDelete=confirm("是否删除类别编号为"+classGradeId+"的小类别");
	if(isDelete){
		$.ajax({
			url:'admin/deletesmallcalss',
			type:'post',
			data:{
				smallClassId:classGradeId
			},
			success:function(msg){
				alert(msg.info);
				addSmallClass(0);
			}
		})
	}
}

//添加商品列表
function addGoods(page){
	$.ajax({
		url:'admin/getgoods',
		data:{page:page},
		success:function(msg){
			$('#goodsTable').dataTable().fnDestroy();
			$("#goodslist").empty();
			var string="";
			for(var i=0;i<msg.list.length;i++)
			{
				var status="<span style='color:red'>违规下架</span>";
				var btn="<button onclick='unBannedGoods("+msg.list[i].goodsId+")' class='btn btn-success btn-xs'>上架</button>";
				if(msg.list[i].goodsStatus!=-1){
					btn="<button onclick='bannedGoods("+msg.list[i].goodsId+")' class='btn btn-danger btn-xs'>违规下架</button>";
					status="<span style='color:green'>正常</span>";
				}
				if(msg.list[i].goodsStatus!=-1&&msg.list[i].goodsStatus!=1){
					btn="";
				}
				string+="<tr>"+
						"<td>"+msg.list[i].goodsId+"</td>"+
						"<td>"+msg.list[i].userId+"</td>"+
						"<td>"+msg.list[i].bigClass+'-'+msg.list[i].smallClass+"</td>"+
						"<td>"+msg.list[i].goodsName+"</td>"+
						"<td>"+msg.list[i].goodsDescribe+"</td>"+
						"<td>"+msg.list[i].goodsTime+"</td>"+             
						"<td>"+status+"</td>"+             
						"<td>"+btn+"</td>"+
						"</tr>";
			}
			$("#goodslist").append(string);
			$('#goodsTable').dataTable({
	            "bFilter": false,
	            "paging": false,
	            "info": false
	        });
			TOTALGOODS=msg.total;
			$("#uppagegoods").click(function(){
				if(INDEXGOODS>1){
					INDEXGOODS--;
					addGoods(INDEXGOODS);
				}
			})
			$("#downpagegoods").click(function(){
				if(TOTALGOODS>INDEXGOODS){
					INDEXGOODS++;
					addGoods(INDEXGOODS);
				}
			})
		}
	})
}




//封禁
function bannedGoods(uid){
	$.ajax({
		url:'admin/bannedgoods',
		type:'post',
		data:{
			goodsId:uid
		},
		success:function(msg){
			if(msg.status!=1){alert(msg.info);return;}
			addGoods(INDEXGOODS);
		}
	})
}
//解封
function unBannedGoods(uid){
	$.ajax({
		url:'admin/unbannedgoods',
		type:'post',
		data:{
			goodsId:uid
		},
		success:function(msg){
			if(msg.status!=1){alert(msg.info);return;}
			addGoods(INDEXGOODS);
		}
	})
}