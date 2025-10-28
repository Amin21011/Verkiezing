/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html","./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme:{
    extend:{
      colors:{
        paper:"#fdfcf7", ink:"#1c1c1c",
        graymain:"#555555", accent:"#e63946",
        highlight:"#fffae5"
      },
      fontFamily:{
        headline:['"Playfair Display"',"serif"],
        body:['"Merriweather"',"serif"],
        mono:['"IBM Plex Mono"',"monospace"],
      },
      boxShadow:{
        press:"-6px 6px 0 #1c1c1c",
        soft:"0 4px 30px rgba(0,0,0,0.1)",
      }
    }
  },
  plugins:[]
}
