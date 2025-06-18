<template>
  <div>
    <h2>就业形式分布柱状图</h2>
    <!-- 使用 :data 代替 :chart-data -->
    <Bar v-if="chartData.labels.length" :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Bar } from 'vue-chartjs'
import { Chart, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend } from 'chart.js'

// 注册所需的 Chart.js 组件
Chart.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend)

export default {
  name: 'EmploymentBarChart',
  components: {
    Bar
  },
  data() {
    return {
      labels: [
        '签劳动合同形式就业',
        '签就业协议形式就业',
        '就业&升学其他录用形式就业',
        '国家基层项目',
        '地方基层项目',
        '应征义务兵',
        '自主创业',
        '自由职业',
        '境内升学',
        '出国、出境（含升学、就业）',
        '未就业待就业',
        '不就业拟升学',
        '其他暂不就业'
      ],
      dataValues: [
        63.0,
        5.7,
        12.9,
        0.2,
        0.9,
        0.8,
        0.8,
        5.5,
        1.3,
        0.7,
        7.9,
        0.1,
        0.1
      ],
      chartData: {
        labels: [],
        datasets: []
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          },
          title: {
            display: true,
            text: '就业形式分布'
          },
          tooltip: {
            callbacks: {
              label: function(context) {
                return `${context.parsed.y}%`
              }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: '百分比 (%)'
            }
          },
          x: {
            title: {
              display: true,
              text: '就业形式'
            }
          }
        }
      }
    }
  },
  mounted() {
    this.renderChartData()
  },
  methods: {
    renderChartData() {
      this.chartData = {
        labels: this.labels,
        datasets: [
          {
            label: '就业比例 (%)',
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
            data: this.dataValues
          }
        ]
      }
    }
  }
}
</script>

<style scoped>
h2 {
  text-align: center;
  margin-bottom: 20px;
}
div {
  width: 100%;
  height: 500px; /* 可以根据需要调整高度 */
}
</style>
