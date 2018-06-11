var a = {
	chart:{
		borderWidth: 2,
		borderColor: '#A92121',
		borderRadius: 15
	}
}
var b = {
	chart:{
		borderWidth: 2,
		borderColor: '#561',
		borderRadius: 15
	}
}
var c = {
	chart:{
		borderWidth: 2,
		borderColor: '#034462',
		borderRadius: 15
	}
}

function drawChart(chartType) {
	showDataReport();
	Highcharts.setOptions(a);
	if(chartType=='pie'){
		drawPieChart();
		return;
	}
	var rotationValue = 0;
	if(chartType == 'line')
		rotationValue = -45
	chart = new Highcharts.Chart({
		chart: {
			renderTo: 'container',
			defaultSeriesType: chartType
		},
		title: {
			text: dataChartTitle
		},
		subtitle: {
			text: ''
		},
		xAxis: {"categories":dataDataLabel,
			labels: {
				rotation: rotationValue,
				align: 'right',
				style: {
					font: 'normal 13px Verdana, sans-serif'
				}
			}
		},
		yAxis: dataYAxisLabel,
		legend: {
			align: 'right',
			x: -40,
			verticalAlign: 'bottom',
			y: 120,
			floating: true,
			backgroundColor: '#FFFFFF',
			borderColor: '#CCC',
			borderWidth: 1,
			shadow: false
		},
		tooltip: {
			formatter: function() {
				return '' +
						this.x +', '+this.series.name.label+ ': ' + this.y + ' ';
			}
		},
		plotOptions: {
			column: {
				pointPadding: 0.2,
				borderWidth: 0
			}
		},
		series: dataData
	});
}
function drawPieChart(){
	chart = new Highcharts.Chart({
		chart: {
			renderTo: 'container',
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false
		},
		title: {
			text: dataChartTitle
		},
		tooltip: {
			formatter: function() {
				return '<b>'+ this.point.name +'</b>'+', '+this.series.name+': '+ this.y;
			}
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					color: '#000000',
					connectorColor: '#000000',
					formatter: function() {
						return '<b>'+ this.point.name +'</b>: '+ this.y;
					}
				}
			}
		},
		series: [{
			type: 'pie',
			name: dataYAxisLabel[0].title.text,
			data: pieData
		}]
	});
}

function drawViewsChart(){
	Highcharts.setOptions(c);
	viewsChart = new Highcharts.Chart({
		chart: {
			renderTo: 'containerViews',
			type: 'column'
		},
		title: {
			text: viewChartTitle
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			categories:dataLabelViews,
			labels: {
				rotation: -45,
				align: 'right'
			}
		},
		yAxis: {
			min: 0,
			title: {
				text: 'Counts'
			}
		},
		legend: {
			layout: 'vertical',
			backgroundColor: '#FFFFFF',
			align: 'left',
			verticalAlign: 'top',
			x: 100,
			y: 70,
			floating: true,
			shadow: true
		},
		tooltip: {
			formatter: function() {
				return '' +
						this.x +'-'+this.series.name+ ': ' + this.y + ' ';
			}
		},
		plotOptions: {
			column: {
				pointPadding: 0.2,
				borderWidth: 0
			}
		},
		series: yDataArrListViews
	});
}
function drawAccessChart(){
	Highcharts.setOptions(b);
	accessChart = new Highcharts.Chart({
		chart: {
			renderTo: 'containerAccess',
			defaultSeriesType: 'bar'
		},
		title: {
			text: accessChartTitle
		},
		subtitle: {
			text: ''
		},
		xAxis: {"categories":accessDataLabel,
			labels: {
				rotation: 0,
				align: 'right',
				style: {
					font: 'normal 13px Verdana, sans-serif'
				}
			}
		},
		yAxis: yAxisAccessLabels,
		legend: {
			align: 'right',
			x: -40,
			verticalAlign: 'bottom',
			y: 120,
			floating: true,
			backgroundColor: '#FFFFFF',
			borderColor: '#CCC',
			borderWidth: 1,
			shadow: false
		},
		tooltip: {
			formatter: function() {
				return '' +
						this.x +' '+this.series.name+ ': ' + this.y + ' ';
			}
		},
		plotOptions: {
			column: {
				pointPadding: 0.2,
				borderWidth: 0
			}
		},
		series: accessData
	});
}


function showAccessReport(){
	$(".chartDrawn").hide();
	$("#containerAccess").show();
	$(".cap").removeClass("capContainer").removeClass("capContainerViews").addClass("capContainerAccess")
}
function showDataReport(){
	$(".chartDrawn").hide();
	$("#container").show();
	$(".cap").removeClass("capContainerAccess").removeClass("capContainerViews").addClass("capContainer")
}
function showViewsReport(){
	$(".chartDrawn").hide();
	$("#containerViews").show();
	$(".cap").removeClass("capContainerAccess").removeClass("capContainer").addClass("capContainerViews")
}