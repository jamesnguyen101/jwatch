Ext.onReady(function() {

	tabPanel = new Ext.TabPanel({
		region : 'center',
		autoScroll : true,
		enableTabScroll : true,
		border : true,
		frame : false,
		plain : false,
		layoutOnTabChange : true, // IMPORTANT!
		items : [{
					title : '<font style="font-size:14px">About JWatch</font>',
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
				fields : ['host', 'port', 'userName', 'password', 'uuid']
			});
	QIStore.load({
		callback : function(records, options, success) {
			// create tab
			for (var loop = 0; loop < QIStore.getTotalCount(); loop++) {
				if (QIStore.data.items[loop] != undefined) {
					doLoadInstance(QIStore.data.items[loop].data);
					popAlert('Quartz instance <b>'
									+ QIStore.data.items[loop].data.host + ':'
									+ QIStore.data.items[loop].data.port
									+ '</b> loaded successfully', 'Success',
							'icon-n-info', 3000);
				}
			}

			if (QIStore.getTotalCount() <= 0) {
				popAlert(
						'Quartz Instances Not Configured, click on the Add link to get started.',
						'Nothing to see...', 'icon-n-warn', 8000);
			}
			// tabPanel.activeTab = 0;
		}
	});
	var jobStore;
    var triggerStore;
	doLoadInstance = function(instance) {
		// console.log(instance);
		var html = '<div style="background-color:#DCF6D8;height: 42px; width: 100%;">'
				+ '<table width="100%">'
				+ '<tr>'
				+ '<td style="text-align:left" width="200">'
				+ '<div id="sched-select" style="margin-left:-100;"></div>'
				+ '</td><td style="text-align:left">'
				+ '<table><tr><td><div id="infoid"></div></td>'
				+ '<td><div id="refreshid"></div></td></tr></table></td></tr></table></div>'
		jobStore = new Ext.data.JsonStore({
					url : 'ui',
					root : 'data',
					autoLoad : false,
					totalProperty : 'totalCount',
					fields : ['jobName', 'description', 'group', 'jobClass',
							'schedulerInstanceId', 'durability',
							'shouldRecover', 'quartzInstanceId']
				});
		jobStore.setDefaultSort('jobName', 'ASC');
		var jobGrid = new Ext.grid.GridPanel({
			store : jobStore,
			closable : false,
			columns : [{
				id : 'colaction',
				header : '',
				width : 40,
				sortable : false,
				renderer : function(value, p, record) {
					return '<a href="javascript:void(0);" onClick="getTriggersForJob(\''
							+ record.data.quartzInstanceId
							+ '\',\''
							+ record.data.schedulerInstanceId
							+ '\',\''
							+ record.data.jobName
							+ '\',\''
							+ record.data.group
							+ '\')">XX</a>';
				}
			}, {
				id : 'jobName',
				header : "Job",
				width : 160,
				sortable : true,
				dataIndex : 'jobName',
				renderer : function(value, p, record) {
					return '<div style="padding:5px;"><font style="font-size: 13px;font-weight:bold;color:#333333">'
							+ Ext.util.Format.htmlEncode(record.data.jobName)
							+ '</font></div>'
				}
			}, {
				id : 'group',
				header : "Group",
				width : 100,
				sortable : true,
				dataIndex : 'group',
				renderer : function(value, p, record) {
					return '<div style="padding:5px;"><font style="font-size: 13px;font-weight:bold;color:#333333">'
							+ Ext.util.Format.htmlEncode(record.data.group)
							+ '</font></div>'
				}
			}, {
				id : 'jobClass',
				header : "Job Class",
				width : 180,
				sortable : true,
				dataIndex : 'jobClass',
				renderer : function(value, p, record) {
					return '<div style="padding:5px;"><font style="font-size: 13px;font-weight:bold;color:#333333">'
							+ Ext.util.Format.htmlEncode(record.data.jobClass)
							+ '</font></div>'
				}
			}, {
				id : 'durability',
				header : "Durable",
				width : 50,
				sortable : true,
				dataIndex : 'durability',
				renderer : function(value, p, record) {
					if (record.data.durability == true) {
						return '<div style="text-align:center;margin:auto;">'
								+ '<img src="images/ok.png" border="0"></div>';
					}
				}
			}, {
				id : 'shouldRecover',
				header : "Recoverable",
				width : 80,
				sortable : true,
				dataIndex : 'shouldRecover',
				renderer : function(value, p, record) {
					if (record.data.shouldRecover == true) {
						return '<div style="text-align:center;margin:auto;">'
								+ '<img src="images/ok.png" border="0"></div>';
					}
				}
			}],
			stripeRows : true,
			autoScroll : true,
			autoExpandColumn : 'jobName',
			region : 'center',
			floatable : false,
			// width : '60%',
			autoWidth : true,
			height : 350,
			autoHeight : false,
			border : true,
			frame : false,
			loadMask : true
		});
		var triggerGrid = new Ext.Panel({
			title : 'Job Triggers',
			height : 50,
			region : 'south',
			store : triggerStore,
			closable : false,
			columns : [{
				id : 'jobName',
				header : "Job",
				width : 160,
				sortable : true,
				dataIndex : 'jobName',
				renderer : function(value, p, record) {
					return '<div style="padding:5px;"><font style="font-size: 13px;font-weight:bold;color:#333333">'
							+ Ext.util.Format.htmlEncode(record.data.jobName)
							+ '</font></div>'
				}
			}, {
				id : 'group',
				header : "Group",
				width : 100,
				sortable : true,
				dataIndex : 'group',
				renderer : function(value, p, record) {
					return '<div style="padding:5px;"><font style="font-size: 13px;font-weight:bold;color:#333333">'
							+ Ext.util.Format.htmlEncode(record.data.group)
							+ '</font></div>'
				}
			}, {
				id : 'jobClass',
				header : "Job Class",
				width : 180,
				sortable : true,
				dataIndex : 'jobClass',
				renderer : function(value, p, record) {
					return '<div style="padding:5px;"><font style="font-size: 13px;font-weight:bold;color:#333333">'
							+ Ext.util.Format.htmlEncode(record.data.jobClass)
							+ '</font></div>'
				}
			}, {
				id : 'durability',
				header : "Durable",
				width : 50,
				sortable : true,
				dataIndex : 'durability',
				renderer : function(value, p, record) {
					if (record.data.durability == true) {
						return '<div style="text-align:center;margin:auto;">'
								+ '<img src="images/ok.png" border="0"></div>';
					}
				}
			}, {
				id : 'shouldRecover',
				header : "Recoverable",
				width : 80,
				sortable : true,
				dataIndex : 'shouldRecover',
				renderer : function(value, p, record) {
					if (record.data.shouldRecover == true) {
						return '<div style="text-align:center;margin:auto;">'
								+ '<img src="images/ok.png" border="0"></div>';
					}
				}
			}],
			stripeRows : true,
			autoScroll : true,
			autoExpandColumn : 'jobName',
			region : 'center',
			floatable : false,
			border : false,
			frame : false,
			loadMask : true
		});

		var schedPanel = new Ext.Panel({
					id : 'schedPanel' + instance.uuid,
					border : false,
					frame : false,
					html : html,
					region : 'north'
				});

		var sampleTab = new Ext.Panel({
					id : instance.uuid,
					iconCls : 'ico-clock',
					frame : false,
					border : false,
					plain : true,
					autoWidth : true,
					title : '<font style="font-size:14px">' + instance.userName
							+ '@' + instance.host + ':' + instance.port
							+ '</font>',
					// height : 50,
					autoHeight : true,
					closable : true,
					items : [schedPanel, jobGrid, triggerGrid]
				});
		tabPanel.add(sampleTab);
		tabPanel.setActiveTab(instance.uuid);

		// sched-select
		var comboTPL = new Ext.XTemplate(
				'<tpl for="."><div class="multiline-combo">',
				'<img src="images/schedule16x16.png" style="vertical-align:middle"/>&nbsp;<b>{name:htmlEncode}</b></div></tpl>');

		var schedStore = new Ext.data.JsonStore({
					url : 'ui?action=get_schedulers&uuid=' + instance.uuid,
					root : 'data',
					totalProperty : 'totalCount',
					fields : ['name', 'instanceId', 'uuidInstance'],
					autoLoad : false
				});
		schedStore.load({
			callback : function(records, options, success) {
				var schedbox = new Ext.form.ComboBox({
							tpl : comboTPL,
							itemSelector : 'div.multiline-combo',
							mode : 'remote',
							store : schedStore,
							allowBlank : true,
							emptyText : 'Select a Scheduler...',
							selectOnFocus : true,
							width : 180,
							listWidth : 300,
							editable : false,
							name : 'schedName',
							id : 'schedName',
							displayField : 'name',
							valueField : 'uuidInstance',
							hiddenName : 'uuidInstance',
							triggerAction : 'all',
							listeners : {
								'select' : function(combo, record) {
									getJobsForScheduler(record.data.uuidInstance);
								}
							}
						});
				new Ext.form.FormPanel({
							baseCls : 'x-example-form',
							style : {
								margin : 0,
								padding : 10
							},
							border : false,
							renderTo : 'sched-select',
							// html: 'aaaa'
							items : [schedbox]
						});
			}
		});

	}

	getJobsForScheduler = function(id) {
		jobStore.load({
					params : {
						action : 'get_jobs',
						'uuidInstance' : id
					}
				});
		var ii = document.getElementById('infoid');
		ii.innerHTML = '<a href="javascript:void(0);" onClick="getSchedulerInfo(\''
				+ id
				+ '\')"><img src="images/info.png" style="padding-bottom:5px;padding-left:7px;border:0;"/></a>';
		var ir = document.getElementById('refreshid');
		ir.innerHTML = '<a href="javascript:void(0);" onClick="refreshScheduler('
				+ id
				+ ')"><img src="images/refresh.png" style="padding-bottom:5px;padding-left:7px;border:0;"/></a>';
	}

	getSchedulerInfo = function(id) {
		Ext.Ajax.request({
			url : 'ui',
			params : {
				action : 'get_scheduler_info',
				'uuidInstance' : id
			},
			waitMsg : 'Removing User Mapping...',
			success : function(response, request) {
				var res = new Object();
				res = Ext.util.JSON.decode(response.responseText);
				if (res.success == true) {
					var schedulerPanel = new Ext.Panel({
								title : '',
								region : 'center',
								autoHeight : true,
								html : ''
							});
					schedulerWin = new Ext.Window({
						id : 'addInstanceWin',
						title : '<font class="panelTitle">Scheduler Details</font>',
						layout : 'fit',
						width : 620,
						frame : true,
						autoScroll : true,
						height : 380,
						closeAction : 'close',
						plain : true,
						border : true,
						html : ''
					});
					schedulerWin.show(document.body);

					var html = '<div style="padding:10px;"><table class="panelTbl" border="0" cellpadding="5" cellspacing="5">'
							+ '<tr><td class="label">Scheduler Name:</td><td>'
							+ res.name
							+ '</td></tr>'
							+ '<tr><td class="label">Job Store Class:</td><td>'
							+ res.jobStoreClassName
							+ '</td></tr>'
							+ '<tr><td class="label">Scheduler ID:</td><td>'
							+ res.instanceId
							+ '</td></tr>'
							+ '<tr><td class="label">Canonical Name:</td><td>'
							+ res.objectName.canonicalName
							+ '</td></tr>'
							+ '<tr><td class="label">Domain:</td><td>'
							+ res.objectName.domain
							+ '</td></tr>'
							+ '<tr><td class="label">Started:</td><td>'
							+ res.started
							+ '</td></tr>'
							+ '<tr><td class="label">Shutdown:</td><td>'
							+ res.shutdown
							+ '</td></tr>'
							+ '<tr><td class="label">Standby Mode:</td><td>'
							+ res.standByMode
							+ '</td></tr>'
							+ '<tr><td class="label">ThreadPool Class:</td><td>'
							+ res.threadPoolClassName
							+ '</td></tr>'
							+ '<tr><td class="label">ThreadPool Size:</td><td>'
							+ res.threadPoolSize
							+ '</td></tr>'
							+ '</table></div>'
					schedulerWin.body.update(html);
				}
			}
		});
	}

	getTriggersForJob = function(qid, sis, jname, gname) {
		console.log(qid);
	}
});