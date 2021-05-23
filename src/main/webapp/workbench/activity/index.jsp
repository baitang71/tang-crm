<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<base href=<%=basePath%>/>
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	/*
	* pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
	* 			,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
	*
	* */
	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#editBtn").click(function (){
			var $cbs=$("input[name=cb]:checked")
			if ($cbs.length==0){
				alert("请勾选需要修改的市场活动")
			}else if ($cbs.length>1){
				alert("一次只能修改一项")
			}else{
				var activityId=$cbs.val()
				$.ajax({
					url:"workbench/activity/getListAndActivity.do",
					data:{
						"id":activityId
					},
					method:"get",
					dataType:"json",
					success:function (data) {
						if(data.success){

							var html=""
							$.each(data.uList,function (i,n) {
								html+="<option value='"+n.id+"'>"+n.name+"</option>"
							})
							$("#edit-owner").html(html)

							$("#edit-id").val(data.activity.id)
							$("#edit-cost").val(data.activity.cost)
							$("#edit-name").val(data.activity.name)
							$("#edit-startDate").val(data.activity.startDate)
							$("#edit-endDate").val(data.activity.endDate)
							$("#edit-description").val(data.activity.description)

							$("#editActivityModal").modal("show")
						}else{
							alert("获取信息失败")
						}
					}
				})
			}
		})

		$("#updateBtn").click(function (){
			$.ajax({
				url:"workbench/activity/updateActivity.do",
				data:{
					"id":$("#edit-id").val(),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val())
				},
				method:"post",
				dataType:"json",
				success:function (data) {
					if(data.success){
						$("#editActivityModal").modal("hide")
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						$("#editActivityForm")[0].reset()
					}else{
						alert("修改市场活动失败")
					}
				}
			})
		})

		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				method: "post",
				dataType: "json",
				success:function (data) {
					if(data.success){
						//关闭模态窗口，刷新市场活动列表
						$("#createActivityModal").modal("hide")
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//清除之前填的数据
						$("#saveActivityForm")[0].reset()
					}else{
						alert("添加市场活动失败")
					}
				}
			})
		})
		
		$("#addBtn").click(function (){
			//发送添加市场活动请求
			$.ajax({
				url:"workbench/activity/selectAll.do",
				dataType:"json",
				method:"get",
				success:function (data) {
					$("#create-owner").html("")
					$.each(data,function (i,n) {
						$("#create-owner").append("<option value='"+n.id+"'>"+n.name+"</option>")
					})
					var id="${user.id}"
					$("#create-owner").val(id);
				}
			})
			$("#createActivityModal").modal("show");
		})
		
		function pageList(pageNo,pageSize) {
			$("#select-name").val($.trim($("#hidden-name").val()));
			$("#select-owner").val($.trim($("#hidden-owner").val()));
			$("#select-startDate").val($.trim($("#hidden-startDate").val()));
			$("#select-endDate").val($.trim($("#hidden-endDate").val()));
			$.ajax({
				url:"workbench/activity/pageList.do",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"name":$.trim($("#select-name").val()),
					"owner":$.trim($("#select-owner").val()),
					"startDate":$.trim($("#select-startDate").val()),
					"endDate":$.trim($("#select-endDate").val())
				},
				datatype:"json",
				method:"get",
				success:function (data) {
					var html="";
					$.each(data.list,function (i,n) {
						html+='<tr class="active">';
						html+='<td><input type="checkbox" name="cb" value="'+n.id+'"/></td>';
						html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+n.name+'</a></td>';
						html+='<td>'+n.owner+'</td>';
						html+='<td>'+n.startDate+'</td>';
						html+='<td>'+n.endDate+'</td>';
						html+='</tr>';
					})
					$("#list-Body").html(html);
					var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

					$("#activityPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 3, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,

						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});
				}
			})
		}

		$("#pageList-btn").click(function (){
			$("#hidden-name").val($.trim($("#select-name").val()));
			$("#hidden-owner").val($.trim($("#select-owner").val()));
			$("#hidden-startDate").val($.trim($("#select-startDate").val()));
			$("#hidden-endDate").val($.trim($("#select-endDate").val()));
			pageList(1,4)
		})

		/*设置全选全不选操作*/
		$("#check-all").click(function () {
			$("input[name=cb]").prop("checked",this.checked)
		})

		$("#list-Body").on("click",$("input[name=cb]"),function () {
			$("#check-all").prop("checked",$("input[name=cb]").length==$("input[name=cb]:checked").length)
		})

		$("#deleteBtn").click(function () {
			var $cbs=$("input[name=cb]:checked");
			if($cbs.length==0){
				alert("请勾选要删除的市场活动")
			}else{
				var param=""
				for (var i=0;i<$cbs.length;i++){
					param+="id="+$($cbs[i]).val()
					if(i<$cbs.length-1){
						param+="&"
					}
				}
				if(confirm("确定要删除吗?")){
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						dataType:"json",
						method:"post",
						success:function (data) {
							if(data.success){
								alert("删除成功")
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除失败")
							}
						}
					})
				}
			}
		})
		pageList(1,4);
	});
	
</script>
</head>
<body>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="saveActivityForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="editActivityForm">
						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="select-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="select-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="select-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="select-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="pageList-btn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox"  id="check-all"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="list-Body">

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>