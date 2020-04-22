import HttpClient from './../utils/HttpClient'
export function CreateProject(param) {
    return HttpClient.post(`/api/createProject`, param)
}
export function GetProjectList(param) {
    return HttpClient.get(`/api/getProjectList`, param)
}
export function DeleteProject(param) {
    return HttpClient.post(`/api/deleteProject`, param)
}
export function GetProjectDetail(param) {
    return HttpClient.get(`/api/getProjectDetail`, param)
}
export function UploadFile(param) {
    return HttpClient.uploadFile(`/api/uploadFile`, param)
}
export function StartConvert(param) {
    return HttpClient.post(`/api/startConvert`, param)
}
export function GetConvertProgress(param) {
    return HttpClient.get(`/api/getConvertProgress`, param)
}
///OCR分类单据
export function CallOCRSplit(param) {
    return HttpClient.post(`/api/ocrSplit`, param)
}
///OCR提取单据内容
export function CallOCRExtract(param) {
    return HttpClient.post(`/api/ocrExtract`, param)
}
export default {
    CreateProject,
    GetProjectList,
    DeleteProject,
    UploadFile,
    StartConvert,
    GetConvertProgress,
    CallOCRSplit,
    CallOCRExtract
} 