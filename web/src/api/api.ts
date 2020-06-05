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
///OCR提取单据内容
export function CallOCR(param) {
    return HttpClient.post(`/api/ocr`, param)
}

///更新ocr的结果
export function UpdateOCRResult(param) {
    return HttpClient.post(`/api/updateOCRResult`, param)
}

///发布项目的结果到目标服务
export function Publish(param) {
    return HttpClient.post(`/api/publish`, param)
}
export default {
    CreateProject,
    GetProjectList,
    DeleteProject,
    UploadFile,
    StartConvert,
    GetConvertProgress,
    CallOCR,
    Publish,
    UpdateOCRResult
} 