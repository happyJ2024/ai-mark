import HttpClient from './../utils/HttpClient'
export function CreateProject(param) {
    return HttpClient.post(`/api/createProject`, param)
}
export function GetProjectList(param) {
    return HttpClient.get(`/api/getProjectList`, param)
}
export function DeleteProject(param) {
    return HttpClient.post(`/api/deleteProjectList`, { param })
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
export function CallOCR(param) {
    return HttpClient.post(`/api/ocr`, param)
}
export default {
    CreateProject,
    GetProjectList,
    DeleteProject,
    UploadFile,
    StartConvert,
    GetConvertProgress,
    CallOCR
} 