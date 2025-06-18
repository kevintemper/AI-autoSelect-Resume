import gradio as gr
import tensorflow as tf
import numpy as np
import easyocr
import pandas as pd
import re
import os

from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences

# ==== 配置 ====
MODEL_PATH = "resume_model_final.keras"
CSV_PATH = "Resume.csv"
MAX_LEN = 300
NUM_WORDS = 10000

# ==== 加载模型和数据 ====
model = tf.keras.models.load_model(MODEL_PATH)
reader = easyocr.Reader(['en'], gpu=False)
df = pd.read_csv(CSV_PATH)
df['clean_text'] = df['Resume_str'].fillna('').astype(str).apply(lambda x: re.sub(r'[^a-zA-Z0-9 ]', '', x.lower()))

# 提取所有职业类别
all_jobs = sorted(df['Category'].dropna().unique().tolist())

# ==== 文本清洗 ====
def clean_text(text):
    text = str(text).lower()
    return re.sub(r'[^a-zA-Z0-9 ]', '', text)

# ==== 主处理函数 ====
def process_resumes_by_job(image_list, top_k, job_name):
    # 过滤该职位的简历用于 tokenizer
    job_df = df[df['Category'] == job_name]
    tokenizer = Tokenizer(num_words=NUM_WORDS, oov_token="<OOV>")
    tokenizer.fit_on_texts(job_df['clean_text'].tolist())

    results = []
    for image in image_list:
        filename = os.path.basename(image.name)
        ocr_result = reader.readtext(image.name, detail=0)
        full_text = ' '.join(ocr_result)
        clean = clean_text(full_text)
        seq = tokenizer.texts_to_sequences([clean])
        padded = pad_sequences(seq, maxlen=MAX_LEN)

        score = float(model.predict(padded)[0][0])
        results.append((filename, score))

    # 排序返回 Top-K
    results.sort(key=lambda x: x[1], reverse=True)
    top_k = min(top_k, len(results))
    top_resumes = results[:top_k]

    return {f"{name}": f"{score:.4f}" for name, score in top_resumes}

# ==== 构建 Gradio 页面 ====
with gr.Blocks(theme=gr.themes.Soft()) as demo:
    gr.Markdown("## 📄 简历优先级筛选系统（按岗位）")
    gr.Markdown("选择岗位 ➜ 上传多份简历图像 ➜ 自动识别并排序最优Top-K")

    with gr.Row():
        job_dropdown = gr.Dropdown(choices=all_jobs, label="选择岗位", value=all_jobs[0])
        topk_input = gr.Number(label="TopK", value=5, precision=0)

    file_input = gr.File(file_types=[".jpg", ".png"], label="上传简历图像（支持多张）", file_count="multiple")
    submit_btn = gr.Button("开始筛选")

    result_output = gr.JSON(label="🎯 筛选结果（文件名 : 评分）")

    submit_btn.click(fn=process_resumes_by_job, inputs=[file_input, topk_input, job_dropdown], outputs=result_output)

# ==== 启动服务 ====
if __name__ == "__main__":
    demo.launch(server_name="0.0.0.0", server_port=7860)
