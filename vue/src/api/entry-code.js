import {localStorageItem} from '../common/js/const'

let entryCodeCache={
  codeCache: localStorage.getItem(localStorageItem.menuList) ? JSON.parse(localStorage.getItem(localStorageItem.menuList)) : {},
}
export{entryCodeCache}
