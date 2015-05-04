<script src="${pageContext.request.contextPath}/js/operations.js"
	type="text/javascript"></script>
<div id="operationsdiv">
	<div id="rightopsExport">
		<div>
			<h2>You can export the following tables</h2>
			<ul>
				<li>CATEGORY</li>
				<li>CENTER</li>
				<li>CUSTOMER</li>
				<li>DEVICE</li>
				<li>DEVICEASSIGNMENT</li>
				<li>FIELDEXECUTIVE</li>
				<li>GROUPS</li>
				<li>GROUPMAP</li>
				<li>ORDERS</li>
				<li>ORDERCONTENT</li>
				<li>OUTBOUND</li>
				<li>PERMISSIONS</li>
				<li>PERMISSIONMAP</li>				
				<li>USER</li>
			</ul>

		</div>
		<div align="left">
			<div class="fetchButton" >
				<a href="/service/exportDB" id="exportDBTables" type="button" target="_blank">Export Into CSV</a>
			</div>
		</div>
	</div>
</div>
