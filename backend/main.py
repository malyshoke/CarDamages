from flask import Flask, request, jsonify, send_from_directory
from ultralytics import YOLO
from PIL import Image, ImageDraw, ImageFont, ImageOps
from waitress import serve
import os
import uuid
import time
from datetime import datetime

app = Flask(__name__)
model = None
IMAGES_DIR = "static/images"
os.makedirs(IMAGES_DIR, exist_ok=True)

def load_model():
    global model
    model = YOLO("best.pt")

@app.route("/images/<path:filename>")
def download_file(filename):
    return send_from_directory(IMAGES_DIR, filename, as_attachment=True)

@app.route('/register', methods=['POST'])
def register_user():
  
    return jsonify({'user_id': 10}), 200


@app.route("/return4Files", methods=["POST"])
def return_4_files():
    files = request.files.getlist("image_files")
    if len(files) != 4:
        return jsonify(error="Exactly 4 image files are required"), 400

    parts = ['front', 'left', 'back', 'right']
    results = []
    request_time = datetime.now().strftime("%d.%m.%Y %H:%M:%S")

    try:
        for i, file in enumerate(files):
            img = Image.open(file.stream)
            detection_result = detect_objects_on_image(img)
            img = draw_boxes_on_image(img, detection_result if detection_result != "no detections" else [])
            filename = f"{uuid.uuid4().hex}.png"
            filepath = os.path.join(IMAGES_DIR, filename)
            img.save(filepath)
            url = request.url_root + "images/" + filename

            label_counts = {label: 0 for label in ["scratch", "dent", "glass_shatter", "tire_flat", "lamp_broken", "background"]}
            if detection_result == "no detections":
                label_counts["background"] = 1
            else:
                for box in detection_result:
                    label_counts[box["label"]] += 1

            results.append({
                "part": parts[i],
                "urls": url,
                "labels": label_counts,
                "request_time": request_time
            })

        return jsonify(results)
    except Exception as e:
        return jsonify(error=f"General error: {str(e)}"), 500

    

def resize_image(img):
    target_size = (640, 640)
    img = ImageOps.fit(img, target_size, Image.Resampling.LANCZOS)
    return img

def detect_objects_on_image(img, confidence_threshold=0.5):
    global model
    results = model.predict(img, conf=confidence_threshold)  
    result = results[0]
    output = []
    if len(result.boxes) == 0:
        return "no detections"
    for box in result.boxes:
        if box.conf[0].item() < confidence_threshold:
            continue
        x1, y1, x2, y2 = [round(x) for x in box.xyxy[0].tolist()]
        class_id = box.cls[0].item()
        prob = round(box.conf[0].item(), 2)
        label = result.names[class_id].replace(" ", "_")
        output.append({"x1": x1, "y1": y1, "x2": x2, "y2": y2, "label": label, "probability": prob})
    return output


def draw_boxes_on_image(img, boxes):
    draw = ImageDraw.Draw(img)
    font_path = "Montserrat-Medium.ttf"
    font_size = 35
    font = ImageFont.truetype(font_path, font_size)
    for box in boxes:
        x1, y1, x2, y2, label, prob = box['x1'], box['y1'], box['x2'], box['y2'], box['label'], box['probability']
        draw.rectangle([x1, y1, x2, y2], outline="lime", width=2)
        text = f"{label} {prob:.2f}"
        text_bbox = draw.textbbox((x1, y1), text, font=font)
        text_width = text_bbox[2] - text_bbox[0]
        text_height = text_bbox[3] - text_bbox[1]
        draw.rectangle([(x1, y1 - text_height - 4), (x1 + text_width + 4, y1)], fill="green")
        draw.text((x1 + 2, y1 - text_height - 2), text, fill="white", font=font)
    return img

if __name__ == "__main__":
    load_model()
    serve(app, host='0.0.0.0', port=8080)
