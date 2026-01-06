export default {
  darkMode: 'class',
  content: ["./index.html","./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        retro: {
          paper: '#f8f5f0',
          ink: '#1a1a1a',
          red: '#b23a48',
          gold: '#d9c79c',
          blue: '#466b8c',
          paperDark: '#121212',
          inkDark: '#eaeaea'
        }
      },
      fontFamily: {
        retroHead: ['"DM Serif Display"', 'serif'],
        retroBody: ['"Libre Baskerville"', 'serif'],
        retroMono: ['"IBM Plex Mono"', 'monospace']
      }
    }
  },
  plugins: []
}
