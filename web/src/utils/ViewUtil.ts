import { ImageData } from "../store/labels/types";
export class ViewUtil {
    public static fireClickEventOnImagePreview(index: number, selectClassName: string, asyncCheckLoadStatus: boolean, imagesData: ImageData[] = [], callback: () => void = null): void {

        if (asyncCheckLoadStatus == false) {
            var imagePreviewList = document.getElementsByClassName(selectClassName);
            console.log("fireClickEventOnImagePreview:", imagePreviewList);

            if (index < imagePreviewList.length) {
                let element = (imagePreviewList[index] as HTMLElement);
                console.log("fireClickEventOnImagePreview:", index, element);
                element.click();
            }
            if (callback) {
                callback();
            }

            return;
        }
        else {
            let checkInterval = 100;
            let intervalHandler = setInterval(() => {
                let image = imagesData[index];
                if (image.loadStatus == false) {
                    console.log("check image locaStatus==false", imagesData);
                }
                else {
                    console.log("check image locaStatus==true", imagesData);
                    clearInterval(intervalHandler);
                    var imagePreviewList = document.getElementsByClassName(selectClassName);
                    console.log("fireClickEventOnImagePreview:", imagePreviewList);
                    if (index < imagePreviewList.length) {
                        let element = (imagePreviewList[index] as HTMLElement);
                        console.log("fireClickEventOnImagePreview:", index, element);
                        element.click();
                    }
                    if (callback) {
                        callback();
                    }
                }
            }, checkInterval);
        }

    }
    public static fireClickEventOnVerticalEditorButton(selectClassName: string): void {
        let timeout = 100;
        setTimeout(() => {
            var sideNavigationButtonList = document.getElementsByClassName(selectClassName);
            console.log("fireClickEventOnVerticalEditorButton:", sideNavigationButtonList);

            if (sideNavigationButtonList.length > 0) {
                let element = (sideNavigationButtonList[0] as HTMLElement);
                console.log("fireClickEventOnVerticalEditorButton:", element);
                element.click();
            }
        }, timeout);
    }

}