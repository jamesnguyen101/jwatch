Ext.onReady(function() {

	var mainPanel = new Ext.Panel({
				region : 'center',
				title : '<font class="panelTitle">Quartz Instances</font>',
				html : 'Information goes here',
				height : 100,
				width : '100%',
				useArrows : true,
				minHeight : 100,
				collapsible : false,
				border : true
			});

	new Ext.Viewport({
				layout : 'border',
				items : [{
							xtype : 'box',
							region : 'north',
							applyTo : 'headerDiv',
							height : 30
						}, {
							layout : 'border',
							region : 'center',
							border : false,
							split : false,
							items : [mainPanel]
						}],
				renderTo : Ext.getBody()
			});

	Ext.Ajax.request({
				url : 'ui',
				params : {
					action : 'get_all_instances'
				},
				waitMsg : 'Loading configuration...',
				success : function(form, action) {
					var res = new Object();
					res = Ext.util.JSON.decode(form.responseText);
					if (res.success == true) {
						popAlert('Quartz Instances Loaded successfully',
								'Success.',
								'icon-n-info', 3000);
                                if (res.totalCount == 0)
                                {
                                    addInstanceWindow();
                                }

/*						lfMapStore.removeAll();
						lfMapStore.reload();
						lfMapPanel.form.reset();
						crmFieldStore.removeAll();
						crmFieldStore.reload();*/
					} else if (res.success == false) {
/*						Ext.Msg.show({
									msg : '<b>User Mapping removal failed!</b>',
									buttons : Ext.Msg.OK,
									icon : Ext.MessageBox.ERROR
								});*/
					}
				},
				failure : function(form, action) {
						Ext.Msg.show({
								msg : '<b>User Mapping removal failed!</b>',
								buttons : Ext.Msg.OK,
								icon : Ext.MessageBox.ERROR
							});
				}
			});
});