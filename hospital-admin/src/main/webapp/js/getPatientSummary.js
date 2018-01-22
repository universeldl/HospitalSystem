function getSummary() {

    ajax(
        {
            method: 'POST',
            url: 'doctor/patientManageAction_getSummary.action',
            type: "json",
            callback: function (data) {
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('pie'));
                myChart.setOption({
                    title: {
                        text: '答卷患者性别比例',
                        //subtext: '子标题',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['男', '女']
                    },
                    series: [
                        {
                            name: '患者性别',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
                            data: [
                                {value: data.male, name: '男'},
                                {value: data.female, name: '女'}
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
                var myChart = echarts.init(document.getElementById('line_bar'));
                myChart.setOption({
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            crossStyle: {
                                color: '#999'
                            }
                        }
                    },
                    toolbox: {
                        feature: {
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    legend: {
                        data: ['新增患者人数', '患者总人数']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                            axisPointer: {
                                type: 'shadow'
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '新增人数',
                            min: 0,
                            max: 250,
                            interval: 50,
                            axisLabel: {
                                formatter: '{value} 人'
                            }
                        },
                        {
                            type: 'value',
                            name: '总人数',
                            min: 0,
                            max: 1500,
                            interval: 300,
                            axisLabel: {
                                formatter: '{value} 人'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '新增患者人数',
                            type: 'bar',
                            data: [20, 49, 70, 232, 25, 76, 135, 162, 33, 200, 64, 33]
                        },
                        {
                            name: '患者总人数',
                            type: 'line',
                            yAxisIndex: 1,
                            data: [20, 69, 159, 391, 414, 490, 625, 787, 820, 1020, 1084, 1117]
                        }
                    ]
                });
            }
        }
    );

}


