<template>
  <div class="app-container">
    <router-view v-slot="{ Component }">
      <transition name="slide-up" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>

    <!-- 主题切换按钮 -->
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Moon, Sunny } from '@element-plus/icons-vue'

const SunnyIcon = ref(Sunny)
const MoonIcon = ref(Moon)
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body,
#app {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.app-container {
  width: 100%;
  height: 100%;
  background: var(--bg-page);
  transition:
    background-color 0.3s ease,
    color 0.3s ease;
}

.app-container.dark {
  background: var(--bg-dark);
}

.theme-toggle {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: var(--z-fixed);
}

.theme-button {
  padding: 12px;
  font-size: 18px;
  box-shadow: var(--shadow-lg);
  transition: all 0.3s ease;
}

.theme-button:hover {
  transform: rotate(360deg) scale(1.1);
  box-shadow: var(--shadow-xl);
}

.theme-button:active {
  transform: rotate(360deg) scale(1);
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>
