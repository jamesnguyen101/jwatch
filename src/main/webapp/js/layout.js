var tabPanel;
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

	doLoadInstance = function(instance) {
		// var main_did = 'm_' + instance.uuid;

		var html = '<div style="background-color:#DCF6D8;height: 42px; width: 100%;">'
				+ '<table width="100%">'
				+ '<tr>'
				+ '<td width="400">'
				+ '<div style="padding-top: 0;padding-left: 8px;float:left;">'
				+ '<font style="font-size: 16px;font-weight: bold;">'
				+ 'Quartz @ '
				+ instance.host
				+ ':'
				+ instance.port
				+ '</font>'
				+ '<br/>'
				+ '<font style="font-size: 11px;font-weight:normal;">'
				+ '</font>'
				+ '</div>'
				+ '</td>'
				+ '<td align="left" style="padding-right: 30px;padding-top: 0;">'
				+ '<div id="sched-select"></div></td>' + '</tr></table></div>'

		var jobPanel = new Ext.Panel({
					title : '',
					height : 80,
					border : false,
					frame : false
				});
		var schedPanel = new Ext.Panel({
					// title : 'schedPanel',
					border : false,
					frame : false,
					region : 'north',
					html : html,
					region : 'center'

				});

		var sampleTab = new Ext.Panel({
					id : instance.uuid,
					iconCls : 'ico-clock',
					frame : false,
					border : false,
					plain : true,
					width : '100%',
					title : '<font style="font-size:14px">' + instance.userName
							+ '@' + instance.host + ':' + instance.port
							+ '</font>',
					height : 50,
					autoHeight : true,
					closable : true,
					items : [schedPanel, jobPanel]
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
					autoLoad : true
				});
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
							var s = record.data.uuidInstance;
							console.log(uuidInstance);
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
					/*
					 * items : [new Ext.ux.form.DateRange({ value : dStart + '
					 * to ' + dEnd, handler : handleDateSelection })]
					 */});
	}
});