var loadingMessageArr = new Array();
loadingMessageArr[0] = "Raising the volume to Eleven";
loadingMessageArr[1] = "Reticulating Splines";
loadingMessageArr[2] = "Dividing By Zero";
loadingMessageArr[3] = "Calculating the Square Root of -1";
loadingMessageArr[4] = "Plotting AI Strategery";
loadingMessageArr[5] = "Displaying Flippant Loading Message";
loadingMessageArr[6] = "Jumping Sharks";
loadingMessageArr[7] = "Adding More Cowbell";
loadingMessageArr[8] = "Powering Batteries, Speeding Turbines";
loadingMessageArr[9] = "Boom Goes The Dynamite";
loadingMessageArr[10] = "This server is powered by a lemon and two electrodes";
loadingMessageArr[11] = "We're testing your patience";
loadingMessageArr[12] = "Go ahead -- hold your breath";
loadingMessageArr[13] = "Please Wait... and dream of faster computers.";
loadingMessageArr[14] = "at least you're not on hold";
loadingMessageArr[15] = "Time is an illusion. Loading time doubly so.";
loadingMessageArr[16] = "Working... no, just kidding.";
loadingMessageArr[17] = "Behind you! Ha, ha, gotcha!";
loadingMessageArr[18] = "Shovelling coal into the server...";


randomMaskMessage = function()
{
    var i = Math.floor(19 * Math.random())
    return new Ext.LoadMask(Ext.getBody(), {msg: loadingMessageArr[i]});
}
Ext.reg('randomMaskMessage', randomMaskMessage);

randomMaskText = function()
{
    var i = Math.floor(19 * Math.random())
    return '<img src="images/default/grid/loading.gif" style="float:left;padding-right:5px;"/>' + loadingMessageArr[i];
}
Ext.reg('randomMaskText', randomMaskText);