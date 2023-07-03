<template>
  <div class="content-list">
    <div class="list-title">我的借阅</div>
    <a-tabs default-active-key="1" @change="onTabChange">
      <a-tab-pane key="1" tab="全部">
      </a-tab-pane>
      <a-tab-pane key="2" tab="在借">
      </a-tab-pane>
      <a-tab-pane key="3" tab="已还">
      </a-tab-pane>
    </a-tabs>
    <div class="list-content">
      <div class="order-item-view" v-for="(item, index) in borrowData" :key="index">
        <div class="header flex-view">
          <div class="left">
            <span class="text">序号</span>
            <span class="num mg-4">#</span>
            <span class="num">{{item.id}}</span>
            <span class="time">{{getFormatTime(item.orderTime, true)}}</span>
          </div>
          <div class="right">
            <a-popconfirm
                v-if="item.status==='1'"
                title="确定还书？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleReturn(item)"
            >
              <a-button type="primary" size="small" style="margin-right: 24px;">还书</a-button>
            </a-popconfirm>
            <a-popconfirm
                v-if="item.status==='1'"
                title="确定延期？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelay(item)"
            >
              <a-button size="small" style="margin-right: 24px;">延期</a-button>
            </a-popconfirm>
            <span class="text">图书状态</span>
            <span class="state">{{item.status==='1'? '在借': item.status === '2'? '已还':'--'}}</span>
          </div>
        </div>
        <div class="content flex-view">
          <div class="left-list">
            <div class="list-item flex-view">
              <img :src="item.cover" class="thing-img">
              <div class="detail flex-between flex-view">
                <div class="flex-between flex-top flex-view">
                  <h2 class="name">{{item.title}}</h2>
                  <span class="count">{{item.author}}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="right-info">
            <p class="title">借阅人</p>
            <p class="name">{{item.username}}
            </p>
            <p class="title">应还时间</p>
            <p class="text">{{getFormatTime(item.expectTime, true)}}
            </p>
            <p class="title">备注信息</p>
            <p class="text">{{item.remark}}
            </p>
          </div>
        </div>
        <div class="bottom flex-view">
          <div class="left">
            <span class="open" @click="handleDetail(item.thingId)">图书详情</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {message} from "ant-design-vue";
import {getFormatTime} from '/@/utils/'
import {userBorrowListApi, returnUserBorrowApi, delayUserBorrowApi} from '/@/api/borrow'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const loading = ref(false)
const borrowData = ref([])
const borrowStatus = ref('')

onMounted(()=>{
  getDataList()
})

const onTabChange =(key)=> {
  console.log(key)
  if (key === '1') {
    borrowStatus.value = ''
  }
  if (key === '2') {
    borrowStatus.value = '1'
  }
  if (key === '3') {
    borrowStatus.value = '2'
  }
  getDataList()
}
const getDataList= ()=> {
  loading.value = true
  let userId = userStore.user_id
  userBorrowListApi({userId: userId, status: borrowStatus.value}).then(res => {
    res.data.forEach((item, index) => {
      if (item.cover) {
        item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover
      }
    })
    borrowData.value = res.data
    loading.value = false
  }).catch(err => {
    console.log(err)
    loading.value = false
  })
}
const handleDetail =(thingId) =>{
  // 跳转新页面
  let text = router.resolve({name: 'detail', query: {id: thingId}})
  window.open(text.href, '_blank')
}
const handleReturn =(item)=> {
  returnUserBorrowApi({
    id: item.id
  }).then(res => {
    message.success('还书成功')
    getDataList()
  }).catch(err => {
    message.error(err.msg || '操作失败')
  })
}

const handleDelay =(item)=> {
  delayUserBorrowApi({
    id: item.id
  }).then(res => {
    message.success('延期成功')
    getDataList()
  }).catch(err => {
    message.error(err.msg || '操作失败')
  })
}

</script>
<style scoped lang="less">
.flex-view {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.content-list {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    line-height: 24px;
    height: 24px;
    margin-bottom: 4px;
  }
}

.order-item-view {
  background: #f7f9fb;
  border-radius: 4px;
  padding: 16px;
  margin-top: 12px;

  .header {
    border-bottom: 1px solid #cedce4;
    padding-bottom: 12px;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    font-size: 14px;

    .text {
      color: #6f6f6f;
    }

    .mg-4 {
      margin-left: 4px;
    }

    .num {
      font-weight: 500;
      color: #152844;
    }

    .num {
      font-weight: 500;
      color: #152844;
    }

    .time {
      margin-left: 16px;
      color: #a1adc5;
    }

    .state {
      color: #ff7b31;
      font-weight: 600;
      margin-left: 10px;
    }
  }

  .content {
    padding: 12px 0;
    overflow: hidden;

    .left-list {
      overflow: hidden;
      height: 132px;
      -webkit-box-flex: 2;
      -ms-flex: 2;
      flex: 2;
      padding-right: 16px;

      .list-item {
        height: 60px;
        margin-bottom: 12px;
        overflow: hidden;
        cursor: pointer;
      }

      .thing-img {
        width: 48px;
        height: 100%;
        margin-right: 12px;
      }

      .detail {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
        -ms-flex-direction: column;
        flex-direction: column;
      }

      .flex-between {
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        justify-content: space-between;
      }

      .flex-between {
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        justify-content: space-between;
      }

      .flex-top {
        -webkit-box-align: start;
        -ms-flex-align: start;
        align-items: flex-start;
      }

      .name {
        color: #152844;
        font-weight: 600;
        font-size: 14px;
        line-height: 18px;
      }

      .count {
        color: #484848;
        font-size: 12px;
      }

      .flex-between {
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        justify-content: space-between;
      }

      .flex-center {
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
      }

      .type {
        color: #6f6f6f;
        font-size: 12px;
      }

      .price {
        color: #ff7b31;
        font-weight: 600;
        font-size: 14px;
      }
    }

    .right-info {
      -webkit-box-flex: 1;
      -ms-flex: 1;
      flex: 1;
      border-left: 1px solid #cedce4;
      padding-left: 12px;
      line-height: 22px;
      font-size: 14px;

      .title {
        color: #6f6f6f;
      }

      .name {
        color: #152844;
      }

      .text {
        color: #484848;
      }

      .mg {
        margin-bottom: 4px;
      }
    }
  }

  .bottom {
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    border-top: 1px solid #cedce4;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    font-size: 14px;
    padding-top: 14px;

    .text {
      color: #6f6f6f;
    }

    .open {
      color: #4684e2;
      margin-left: 8px;
      cursor: pointer;
    }

    .right {
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
    }

    .text {
      color: #6f6f6f;
    }

    .num {
      color: #152844;
      margin: 0 40px 0 8px;
    }

    .money {
      font-weight: 600;
      font-size: 18px;
      color: #ff7b31;
      margin-left: 8px;
    }
  }

}

.order-item-view:first-child {
  margin-top: 16px;
}

</style>
