/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html","./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme:{
    extend: {
      colors: {
        retro: {
          paper: '#f8f5f0',
          ink: '#1a1a1a',
          red: '#b23a48',
          gold: '#d9c79c',
          blue: '#466b8c'
        }
      },
      fontFamily: {
        retroHead: ['"DM Serif Display"', 'serif'],
        retroBody: ['"Libre Baskerville"', 'serif'],
        retroMono: ['"IBM Plex Mono"', 'monospace']
      },
      boxShadow: {
        retroSoft: '0 6px 25px rgba(0,0,0,0.08)',
        retroInset: 'inset 0 2px 4px rgba(0,0,0,0.06)'
      },
      borderWidth: {
        3: '3px'
      }
    }
  },
  plugins:[]
}
