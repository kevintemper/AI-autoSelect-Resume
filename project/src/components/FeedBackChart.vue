<template>
    <div>
      <Doughnut :data="chartData" :options="chartOptions" />
    </div>
  </template>
  
  <script>
  import { Doughnut } from 'vue-chartjs'
  import { Chart, ArcElement, Tooltip, Legend } from 'chart.js'
  
  // 注册所需的 Chart.js 组件
  Chart.register(ArcElement, Tooltip, Legend)
  
  export default {
    name: 'FeedBackChart',
    components: {
      Doughnut
    },
    data() {
      return {
        chartData: {
          labels: ['很满意', '满意', '不满意', '很不满意'],
          datasets: [
            {
              label: '毕业生满意度',
              backgroundColor: ['#1f7a8c', '#76c893', '#ffb703', '#ff595e'],
              data: [37, 59, 2, 2],
              borderWidth: 1
            }
          ]
        },
        chartOptions: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: true,
              position: 'top'
            },
            title: {
              display: true,
              text: '用人单位对毕业生的总体满意度'
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  let label = context.label || '';
                  if (label) {
                    label += ': ';
                  }
                  label += context.raw + '%';
                  return label;
                }
              }
            }
          }
        }
      }
    }
  }
  </script>
  
  <style scoped>
  div {
    width: 100%;
    height: 400px;
  }
  </style>
  