<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import SeatTooltip from "@/components/SeatTooltip.vue";
import {
  drawSeats,
  detectHover,
  normalizeParty,
  type SeatEntry,
  type SeatPoint
} from "@/helpers/SimulatorHelper";

const props = defineProps<{
  data: SeatEntry[];
  votesPerSeat: number;
}>();

const canvasRef = ref<HTMLCanvasElement|null>(null);
const seatPoints = ref<SeatPoint[]>([]);

const tooltip = ref({
  x: 0,
  y: 0,
  visible: false,
  party: "",
  seats: 0,
  delta: 0
});

function redraw() {
  if (!canvasRef.value) return;
  seatPoints.value = drawSeats(canvasRef.value, props.data);
}

function onMove(e: MouseEvent) {
  if (!canvasRef.value) return;

  const rect = canvasRef.value.getBoundingClientRect();
  const mx = e.clientX - rect.left;
  const my = e.clientY - rect.top;

  const hovered = detectHover(mx, my, seatPoints.value);

  if (!hovered) {
    tooltip.value.visible = false;
    return;
  }

  const match = props.data.find(
    d => normalizeParty(d.party) === hovered
  );

  tooltip.value = {
    x: e.clientX,
    y: e.clientY - 40,
    visible: true,
    party: match?.party ?? "",
    seats: match?.seats ?? 0,
    delta: match?.delta ?? 0
  };
}

onMounted(() => {
  redraw();
  canvasRef.value?.addEventListener("mousemove", onMove);
  canvasRef.value?.addEventListener("mouseleave", () => tooltip.value.visible = false);
});

watch(() => props.data, redraw);
</script>

<template>
  <div class="relative">
    <canvas
      ref="canvasRef"
      width="900"
      height="360"
      class="w-full rounded-xl"
    ></canvas>

    <SeatTooltip
      :x="tooltip.x"
      :y="tooltip.y"
      :visible="tooltip.visible"
      :party="tooltip.party"
      :seats="tooltip.seats"
      :delta="tooltip.delta"
      :votesPerSeat="props.votesPerSeat"
    />

    <div class="absolute inset-0 pointer-events-none opacity-[0.07] bg-[url('https://www.transparenttextures.com/patterns/paper-fibers.png')]"></div>
  </div>
</template>
