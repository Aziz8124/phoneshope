import time
import subprocess
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

# مسار مجلد المشروع
PROJECT_PATH = r"C:\Users\azozs\Desktop\PhoneShope_Final"

class AutoPushHandler(FileSystemEventHandler):
    def on_modified(self, event):
        # تجاهل الملفات المؤقتة
        if event.src_path.endswith(".swp") or event.src_path.endswith("~"):
            return
        print(f"Detected change in: {event.src_path}")
        try:
            subprocess.run(["git", "add", "."], cwd=PROJECT_PATH, check=True)
            subprocess.run(["git", "commit", "-m", "Auto-commit changes"], cwd=PROJECT_PATH, check=True)
            subprocess.run(["git", "push"], cwd=PROJECT_PATH, check=True)
            print("Changes pushed to GitHub successfully!\n")
        except subprocess.CalledProcessError as e:
            print(f"Error: {e}")

if __name__ == "__main__":
    event_handler = AutoPushHandler()
    observer = Observer()
    observer.schedule(event_handler, path=PROJECT_PATH, recursive=True)
    observer.start()
    print(f"Watching changes in {PROJECT_PATH}... Press Ctrl+C to stop.")
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
