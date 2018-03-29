/**
 * Created by QQQ on 2018/3/29.
 */
function replaceSpectialChar(str) {
    str = str.replace(/%/g, "%25");
    str = str.replace(/\&/g, "%26");
    str = str.replace(/\+/g, "%2B");
    return str;
}