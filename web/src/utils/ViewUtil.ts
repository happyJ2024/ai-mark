export class ViewUtil {
    public static fireClickEventOnImagePreview(index: number, selectClassName: string, timeout: number): void {
        setTimeout(() => {
            var imagePreviewList = document.getElementsByClassName(selectClassName);
            console.log("fireClickEventOnImagePreview:", imagePreviewList);

            if (index < imagePreviewList.length) {
                let element = (imagePreviewList[index] as HTMLElement);
                console.log("fireClickEventOnImagePreview:", index, element);
                element.click();
            }
        }, timeout);
    }
    public static fireClickEventOnVerticalEditorButton(selectClassName: string, timeout: number): void {
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