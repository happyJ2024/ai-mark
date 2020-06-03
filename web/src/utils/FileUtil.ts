import uuidv1 from 'uuid/v1';
import { ImageData } from "../store/labels/types";

export class FileUtil {
    public static mapFileDataToImageData(fileData: File): ImageData {
        return {
            id: uuidv1(),
            fileData: fileData,
            loadStatus: false,
            labelRects: [],
            labelPoints: [],
            labelPolygons: [],
            isVisitedByObjectDetector: false,
            isVisitedByPoseDetector: false, imgUrl: ''
        }
    }
    public static mapUrlToImageData(imgUrl: string): ImageData {

        return {
            id: uuidv1(),
            fileData: null,
            loadStatus: false,
            labelRects: [],
            labelPoints: [],
            labelPolygons: [],
            isVisitedByObjectDetector: false,
            isVisitedByPoseDetector: false,
            imgUrl: imgUrl
        }

        // var image = new Image();
        // image.src = imgUrl;
        // image.setAttribute("crossOrigin", "Anonymous");
        // image.onload = function () {
        //     var base64 = FileUtil.getBase64Image(image);
        //     console.log(base64);
        //     //转换base64到file
        //     var file = FileUtil.btof(base64, "test");
        //     return {
        //         id: uuidv1(),
        //         fileData: file,
        //         loadStatus: false,
        //         labelRects: [],
        //         labelPoints: [],
        //         labelPolygons: [],
        //         isVisitedByObjectDetector: false,
        //         isVisitedByPoseDetector: false
        //     }
        // };
    }
    static getBase64Image(img) {
        var canvas = document.createElement("canvas");
        canvas.width = img.width;
        canvas.height = img.height;
        var ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, img.width, img.height);
        var ext = img.src.substring(img.src.lastIndexOf(".") + 1).toLowerCase();
        var dataURL = canvas.toDataURL("image/" + ext);

        return dataURL;
    }
    static btof(data, fileName) {
        const dataArr = data.split(",");
        const byteString = atob(dataArr[1]);

        const options = {
            type: "image/jpeg",
            endings: 'native',
            lastModified: 0
        };

        const u8Arr = new Uint8Array(byteString.length);
        for (let i = 0; i < byteString.length; i++) {
            u8Arr[i] = byteString.charCodeAt(i);
        }
        return new File([u8Arr], fileName + ".jpg");
    }

    public static loadImage(imageData: ImageData, onSuccess: (image: HTMLImageElement) => any, onFailure: () => any): any {

        return new Promise((resolve, reject) => {
            if (imageData === undefined || imageData === null) {
                onFailure();
                reject();
            }
            let url = null;
            if (imageData.fileData) {
                url = URL.createObjectURL(imageData.fileData);
            } else if (imageData.imgUrl) {
                url = imageData.imgUrl;
            }
            const image = new Image();
            image.setAttribute("crossOrigin", 'Anonymous')
            image.src = url;

            image.onload = () => {
                if (imageData.imgUrl) {

                    var base64 = FileUtil.getBase64Image(image);
                    // console.log(base64);

                    var fileName = imageData.imgUrl.substring(imageData.imgUrl.lastIndexOf("/")+1, imageData.imgUrl.lastIndexOf('.'));
                    //转换base64到file
                    var file = FileUtil.btof(base64, fileName);
                    imageData.fileData = file;

                }

                onSuccess(image);
                resolve();
            };
            image.onerror = () => {
                onFailure();
                reject();
            };
        }
        )

    }

    public static loadLabelsList(fileData: File, onSuccess: (labels: string[]) => any, onFailure: () => any) {
        const reader = new FileReader();
        reader.readAsText(fileData);
        reader.onloadend = function (evt: any) {
            const contents: string = evt.target.result;
            onSuccess(contents.split(/[\r\n]/));
        };
        reader.onerror = () => onFailure();
    }
}