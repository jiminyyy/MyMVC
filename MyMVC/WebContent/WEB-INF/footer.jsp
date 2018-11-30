<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	</div>
	
	<div id="footer">
		<div class="row">
			<div class="col-md-12" style="width: 100%; text-align: center; padding: 3%;">
				4. Copyright
			</div>
		</div>
	</div>
	
</div>

<%-- *** 매장위치 찾기 Modal --%>
<div class="modal fade" id="searchStore" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content" align="center">
        <div class="modal-header">
          <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">매장위치 찾기</h4>
        </div>
        <div class="modal-body" style="width: 100%; height: 400px">
          <div id="idFind">
          	<iframe style="border: none; width: 100%; height: 380px;" src="<%= request.getContextPath() %>/searchStoreMap.do"></iframe>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>

</body>
</html>
