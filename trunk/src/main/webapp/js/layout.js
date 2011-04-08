Ext.onReady(function() {
			new Ext.Viewport({
						layout : 'border',
						// style: 'background: #fff; text-align:left;',
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
									items : [{
												region : 'west',
												collapsible : true,
												bodyStyle : 'padding:3px;',
												title : 'Navigation',
												split : true,
												height : 100,
												width : 200,
												useArrows : true,
												minHeight : 100,
												collapsible : true,
												animCollapse : true,
												border : true,
												autoScroll : true,
												animate : true,
												enableDD : false
											}, {
												region : 'center',
												title : 'Title for Panel',
												html : 'Information goes here',
												height : 100,
												width : 500,
												useArrows : true,
												minHeight : 100,
												collapsible : false,
												border : true
											}]
								}],
						renderTo : Ext.getBody()
					});
		});