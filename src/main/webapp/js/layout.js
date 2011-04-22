var tabPanel;
Ext.onReady(function() {

	tabPanel = new Ext.TabPanel({
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
				fields : ['host', 'port', 'userName', 'password', 'uuid']
			});
	QIStore.on('load', function(store, records, options) {

		// create tab
		for (var loop = 0; loop < store.getTotalCount(); loop++) {
			if (store.data.items[loop] != undefined) {
				doLoadInstance(store.data.items[loop].data);
			}
		}

		/*
		 * // populate tab for (var loop = 0; loop < store.getTotalCount();
		 * loop++) { if (store.data.items[loop] != undefined) {
		 * getInstanceDetails(store.data.items[loop].data); } }
		 */

		if (store.getTotalCount() > 0) {
			popAlert('Quartz Instances Loaded successfully', 'Success',
					'icon-n-info', 3000);
		} else {
			popAlert(
					'Quartz Instances Not Configured, click on the Add link to get started.',
					'Nothing to see...', 'icon-n-warn', 8000);
		}
		tabPanel.activeTab = 0;
	});
	QIStore.load();

	doLoadInstance = function(instance) {

		var main_did = 'm_' + instance.id;

		// console.log("loading: " + id);
		var sampleTab = new Ext.Panel({
					id : instance.id,
					iconCls : 'ico-clock',
					frame : false,
					border : false,
					plain : false,
					width : '100%',
					title : '<font class="panelTitle">' + instance.userName
							+ '@' + instance.host + ':' + instance.port
							+ '</font>',
					height : 50,
					autoHeight : true,
					closable : true,
					html : '<p class="panel-info"><div id="' + main_did
							+ '">Q</div></p>'
				});

		var iStore = new Ext.data.JsonStore({
					url : 'ui?action=get_all_instances',
					root : 'data',
					totalProperty : 'totalCount',
					fields : ['host', 'port', 'userName', 'password', 'uuid']
				});
		iStore.on('load', function(store, records, options) {

			// create tab
			for (var loop = 0; loop < store.getTotalCount(); loop++) {
				if (store.data.items[loop] != undefined) {
					doLoadInstance(store.data.items[loop].data);
				}
			}

			if (store.getTotalCount() > 0) {
				popAlert('Quartz Instances Loaded successfully', 'Success',
						'icon-n-info', 3000);
			} else {
				popAlert(
						'Quartz Instances Not Configured, click on the Add link to get started.',
						'Nothing to see...', 'icon-n-warn', 8000);
			}
			tabPanel.activeTab = 0;
		});
		iStore.load();

		tabPanel.add(sampleTab);
	}
});