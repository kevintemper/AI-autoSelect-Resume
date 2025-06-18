<template>
    <div>
      <Pie :data="chartData" :options="chartOptions" />
    </div>
  </template>
  
  <script>
  import { Pie } from 'vue-chartjs'
  import { Chart, ArcElement, Tooltip, Legend } from 'chart.js'
  
  // 注册所需的 Chart.js 组件
  Chart.register(ArcElement, Tooltip, Legend)
  
  export default {
    name: 'GraduatesChart',
    components: {
      Pie
    },
    data() {
      return {
        chartData: {
          labels: ['网络工程', '信息管理与信息系统'],
          datasets: [
            {
              label: '毕业生人数',
              backgroundColor: ['#36A2EB', '#FF6384'],
              data: [296, 138]
            }
          ]
        },
        chartOptions: {
          responsive: true,
          plugins: {
            legend: {
              display: true,
              position: 'top'
            },
            title: {
              display: true,
              text: '网络技术系毕业生数据'
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  return `${context.label}: ${context.raw} 人 (${((context.raw / 434) * 100).toFixed(2)}%)`
                }
              }
            }
          }
        }
      }
    }
  }
  </script>
  
  <style lang="less" scoped>
  div {
    width: 100%;
    height: 400px;
  }
  </style>
  