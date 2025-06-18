<!-- EmploymentMap.vue -->
<template>
    <div id="employmentMapContent"></div>
  </template>
  
  <script>
  import { onMounted, nextTick } from 'vue';
  import * as echarts from 'echarts';
  import ChinaJson from '../assets/China.json'; // 确保路径和大小写正确
  
  // 注册中国地图
  echarts.registerMap("china", ChinaJson);
  
  export default {
    name: 'EmploymentMap',
    props: {
      employmentData: {
        type: Array,
        required: true,
        default: () => [
          { name: '北京市', value: 37 },
          { name: '山西省', value: 2 },
          // ...其他省份的数据
          { name: '广东省', value: 2928 }
        ]
      }
    },
    methods: {
      drawEmploymentMap(dom) {
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: (params) => {
              const name = params.name;
              const dataItem = this.employmentData.find(item => item.name === name);
              const value = dataItem ? dataItem.value : 0;
              return `${name}: ${value} 人`;
            }
          },
          visualMap: {
            min: 0,
            max: 3000,
            left: 'left',
            top: 'bottom',
            text: ['高', '低'],
            inRange: {
              color: ['#e0ffff', '#006edd']
            },
            calculable: true
          },
          geo: {
            map: 'china',
            roam: true,
            label: {
              show: true,
              color: '#000'
            },
            itemStyle: {
              areaColor: '#f0f0f0',
              borderColor: '#999'
            },
            emphasis: {
              itemStyle: {
                areaColor: '#c9d6ff'
              }
            }
          },
          series: [
            {
              name: '就业人数',
              type: 'map',
              map: 'china',
              geoIndex: 0,
              data: this.employmentData,
              label: {
                show: true,
                color: '#000'
              },
              itemStyle: {
                color: '#FF6F61'
              }
            }
          ]
        };
  
        const echartObj = echarts.init(dom);
        echartObj.setOption(option);
      }
    },
    mounted() {
      nextTick(() => {
        const dom = document.getElementById("employmentMapContent");
        if (dom) {
          this.drawEmploymentMap(dom);
        } else {
          console.error("容器元素未找到！");
        }
      });
    }
  };
  </script>
  
  <style scoped>
  #employmentMapContent {
    width: 600px;
    height: 450px;
    /* padding-top: 20px; */
    box-sizing: border-box;
  }
  </style>
  