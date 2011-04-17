Ext.onReady(function() {

	var tabPanel = new Ext.TabPanel({
		region : 'center',
		activeTab : 0,
		autoScroll : true,
		enableTabScroll : true,
		border : false,
		frame : false,
		plain : false,
		layoutOnTabChange : true, // IMPORTANT!
		items : [{
					title : '<font class="panelTitle">About JWatch</font>',
					// bodyStyle : 'padding-bottom:5px;background:#eee;',
					autoScroll : true,
					closable : true,
					html : '<div style="text-align:left;padding-top:10px;">TODO</div>'
				}]
	});
	new Ext.Viewport({
				layout : 'border',
				items : [{
							xtype : 'box',
							region : 'north',
							applyTo : 'headerDiv',
							height : 60
						}, {
							layout : 'border',
							region : 'center',
							border : false,
							split : false,
							items : [tabPanel]
						}],
				renderTo : Ext.getBody()
			});

	var QIStore = new Ext.data.JsonStore({
				url : 'ui?action=get_all_instances',
				root : 'data',
				totalProperty : 'totalCount',
				fields : ['name']
			});
	QIStore.on('load', function(store, records, options) {
				if (store.getTotalCount() > 0) {
					console.log(store.getTotalCount());
					var baseEl = document.getElementById('div-baseContent');
					baseTPL.overwrite('div-baseContent', records);
					Ext.fly(baseEl).slideIn('t', {
								stopFx : true,
								duration : 1
							});
				} else {
					console.log('0');
				}
				popAlert('Quartz Instances Loaded successfully', 'Success.',
						'icon-n-info', 3000);

				for (var i = 0; i < 3; i++) {
					doLoadInstance(i);
				}
				tabPanel.activeTab = 0;
			});
	QIStore.load();

	doLoadInstance = function(id) {
		var sampleTab = new Ext.Panel({
					id : 'foo' + id,
					iconCls : 'ico-clock',
					frame : false,
					border : false,
					plain : false,
					width : '100%',
					title : '<font class="panelTitle">foo' + id + '</font>',
					height : 50,
					autoHeight : true,
					closable : true,
					html : '<p class="panel-info"></p>'
				});
		tabPanel.add(sampleTab);
	}

	/***************************************************************************
	 * Add new instance panel/window
	 **************************************************************************/
	var addInstanceWin;
	addInstance = function() {
		if (!addInstanceWin) {
			var addInstanceHelp = new Ext.Panel({
				html : '<p class="help-box">TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO</p>',
				region : 'north',
				border : false,
				autoHeight : true,
				width : '100%',
				frame : false
			});
			var addInstancePanel = new Ext.FormPanel({
				border : false,
				region : 'center',
				frame : false,
				plain : false,
				monitorValid : true,
				bodyStyle : 'padding:5px 5px 0',
				items : [new Ext.form.FieldSet({
							border : false,
							frame : false,
							defaultType : 'textfield',
							autoHeight : true,
							width : '400px',
							buttonAlign : 'center',
							items : [{
										fieldLabel : 'Host',
										name : 'host',
										width : 190,
										allowBlank : false,
										required : true,
										maxLength : 254,
										blankText : 'Host cannot be left blank.'
									}, {
										fieldLabel : 'Port',
										name : 'port',
										width : 190,
										maxLength : 254
									}]
						})],
				buttons : [{
					formBind : true,
					text : 'Save New Instance',
					iconCls : 'icon-ok',
					handler : function() {
						addInstancePanel.form.submit({
									url : '/ui',
									params : {
										data : Ext.util.JSON
												.encode(addInstancePanel.form
														.getValues())
									},
									waitMsg : 'Saving Settings...',
									success : function(form, action) {
										popAlert('TODO DESC', 'TODO',
												'icon-n-info', 3000);
										addInstanceWin.hide();
										addInstancePanel.form.reset();
									},
									failure : function(form, action) {
										Ext.Msg.show({
													msg : 'Settings save failed!',
													buttons : Ext.Msg.OK,
													icon : Ext.MessageBox.ERROR
												});
										addInstanceWin.hide();
									}
								});
					}
				}]
			});
			addInstanceWin = new Ext.Window({
						id : 'addInstanceWin',
						title : '<font class="panelTitle">Add Quartz Instance</font>',
						layout : 'border',
						width : 400,
						frame : true,
						height : 500,
						closeAction : 'hide',
						plain : false,
						border : true,
						items : [addInstanceHelp, addInstancePanel]
					});
		}
		addInstanceWin.show('addInstanceLink');
	}
});