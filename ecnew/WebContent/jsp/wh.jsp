<script src="${pageContext.request.contextPath}/js/warehouse.js"
	type="text/javascript"></script>
<%
    String chooseCenter = (String) request.getSession().getAttribute("centerName");
%>
<div class="fulldiv">
	<div>
		<div id="wh-fragment-link">
			<h3 id="reconcileInboundHead">Reconcile - Inbound</h3>
			<div id="reconcileInboundDiv">

				<div>
					<div id="reconcileInbound" class="tablediv" align="center"></div>
				</div>

				<div id="deviceInfoFormBody" class="deviceInfoFormBody">
					<label>Device ID</label> <input type="text" name="deviceID"
						id="deciveIDNo"><br /> <input type="button"
						value="Search" id="searchOrdersForDevice" />
					<div></div>
				</div>
				<div id="deviceOrdersDiv"></div>
			</div>

			<h3 id="reconcileOutboundHead">Reconcile - Outbound</h3>
			<div id="reconcileOutboundDiv">

				<div id="reconcileOutbound" class="tablediv" align="center"></div>
				<hr />
				<div id="messageLoad"></div>
				<div class="orderFormBody">
					<div class="builtLoadForm">
						<form id="loadForm">
							<div class="inputarea">
								<label>NEWS PAPER</label><input type="text" name="newsPaper"
									size="20" maxlength="10" /><input type="hidden"
									name="warehouse" value="<%=chooseCenter%>" size="20"
									maxlength="10" />
							</div>
							<div class="inputarea">
								<label>BOOKS N COPIES</label><input type="text" name="books"
									size="20" />
							</div>
							<div class="inputarea">
								<label>IRON</label><input type="text" name="iron" size="20" />
							</div>
							<div class="inputarea">
								<label>PLASTIC</label><input type="text" name="plastic"
									size="20" />
							</div>
							<div class="inputarea">
								<label>GLASS BOTTLE</label><input type="text" name="gBottle"
									size="20" maxlength="10" />
							</div>
							<div class="inputarea">
								<label>BEER BOTTLE</label><input type="text" name="bBottle"
									size="20" />
							</div>
							<div class="inputarea">
								<label>CARTON</label><input type="text" name="carton" size="20" />
							</div>
							<div class="inputarea">
								<label>ALUMINIUM</label><input type="text" name="aluminium"
									size="20" />
							</div>
							<div class="inputarea">
								<label>STEEL</label><input type="text" name="steel" size="20"
									maxlength="10" />
							</div>
							<div class="inputarea">
								<label>COPPER</label><input type="text" name="copper" size="20" />
							</div>
							<div class="inputarea">
								<label>BRASS</label><input type="text" name="brass" size="20" />
							</div>
						</form>
					</div>
				</div>
				<div class="deviceInfoFormBody">
					<input type="button" value="Built Load" id="buildLoadBtn" />
					<div></div>
				</div>

			</div>
		</div>
	</div>
</div>
