/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#f0f7ff',
          100: '#e0effe',
          500: '#0072ff', // Matching your Ace Digitals theme branding colors
          600: '#005ed4',
          900: '#032b69',
        }
      }
    },
  },
  plugins: [],
}
