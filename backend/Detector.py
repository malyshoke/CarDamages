import numpy as np
from ultralytics import YOLO
import cv2
class Detector:
    def __init__(self, model_file='yolov10.pt', labels=None, classes=None, conf=0.25, iou=0.45):
        self.classes = classes
        self.conf = conf
        self.iou = iou

        self.model = YOLO(model_file)
        self.model_name = model_file.split('.')[0]
        self.results = None

        if labels == None:
            self.labels = self.model.names
        else:
            self.labels = labels

    def predict_img(self, img, verbose=True):
        results = self.model(img, classes=self.classes, conf=self.conf, iou=self.iou, verbose=verbose)
        self.orig_img = img
        self.results = results[0]
        return results[0]

    def custom_display(self, colors, show_cls=True, show_conf=True):
        img = self.orig_img
        bbx_thickness = (img.shape[0] + img.shape[1]) // 450

        for box in self.results.boxes:
            textString = ""

            score = box.conf.item()
            class_id = int(box.cls.item())

            x1, y1, x2, y2 = np.squeeze(box.xyxy.numpy()).astype(int)

            if show_cls:
                textString += f"{self.labels[class_id]}"

            if show_conf:
                textString += f" {score:,.2f}"

            font = cv2.FONT_HERSHEY_COMPLEX
            fontScale = (((x2 - x1) / img.shape[0]) + ((y2 - y1) / img.shape[1])) / 2 * 2.5
            fontThickness = 1
            textSize, baseline = cv2.getTextSize(textString, font, fontScale, fontThickness)

            img = cv2.rectangle(img, (x1, y1), (x2, y2), colors[class_id], bbx_thickness)

            if textString != "":
                if (y1 < textSize[1]):
                    y1 = y1 + textSize[1]
                else:
                    y1 -= 2
                img = cv2.rectangle(img, (x1, y1), (x1 + textSize[0], y1 - textSize[1]), colors[class_id], cv2.FILLED)
                img = cv2.putText(img, textString,
                                  (x1, y1), font,
                                  fontScale, (0, 0, 0), fontThickness, cv2.LINE_AA)

        return img