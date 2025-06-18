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
  data() {
    return {
      employmentData: [
        { name: '北京市', value: 37 },
        { name: '山西省', value: 2 },
        { name: '吉林省', value: 2 },
        { name: '黑龙江省', value: 2 },
        { name: '上海市', value: 21 },
        { name: '江苏省', value: 13 },
        { name: '浙江省', value: 31 },
        { name: '安徽省', value: 2 },
        { name: '福建省', value: 9 },
        { name: '江西省', value: 4 },
        { name: '山东省', value: 6 },
        { name: '河南省', value: 9 },
        { name: '湖北省', value: 3 },
        { name: '湖南省', value: 13 },
        { name: '广东省', value: 2928 },
        { name: '广西壮族自治区', value: 2 },
        { name: '海南省', value: 10 },
        { name: '重庆市', value: 5 },
        { name: '四川省', value: 4 },
        { name: '贵州省', value: 1 },
        { name: '陕西省', value: 7 },
        { name: '宁夏回族自治区', value: 3 },
        { name: '香港特别行政区', value: 2 },
        { name: '境外（不含港澳台）', value: 12 }
      ]
    };
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
  width: 800px;
  height: 600px;
  padding-top: 20px;
  box-sizing: border-box;
}
</style>
