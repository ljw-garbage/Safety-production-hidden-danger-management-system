// module.exports = ({ file }) => {
//   const designWidth = file.dirname.includes('node_modules/vant') ? 375 : 750;
//
//   return {
//     plugins: {
//       autoprefixer: {},
//       "postcss-px-to-viewport": {
//         unitToConvert: "px",
//         viewportWidth: designWidth,
//         unitPrecision: 3,
//         propList: ["*","!letter-spaceing"],
//         viewportUnit: "vw",
//         fontViewportUnit: "vw",
//         selectorBlackList: ['.ignore', '.hairlines'],
//         minPixelValue: 1,
//         mediaQuery: true,
//         exclude: [],
//         landscape: false
//       }
//     }
//   }
//
// }
