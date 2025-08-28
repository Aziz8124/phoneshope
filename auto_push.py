import time
import subprocess
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler
import os

# مسار مجلد المشروع
PROJECT_PATH = r"C:\Users\azozs\Desktop\PhoneShope_Final"

# الملفات/المجلدات لتجاهلها
IGNORE_PATHS = ['.idea', 'target', '.git', '__pycache__']

class AutoPushHandler(FileSystemEventHandler):
    def on_modified(self, event):
        # تجاهل الملفات أو المجلدات المحددة
        if any(ignore in event.src_path for ignore in IGNORE_PATHS):
            return
        if event.src_path.endswith(".swp") or event.src_path.endswith("~"):
            return

        print(f"Detected change in: {event.src_path}")
        try:
            # Git add
            subprocess.run(["git", "add", "."], cwd=PROJECT_PATH, check=True)

            # Git commit
            result = subprocess.run(
                ["git", "commit", "-m", "Auto-commit changes"], 
                cwd=PROJECT_PATH, capture_output=True, text=True
            )
            
            if "nothing to commit" in result.stdout:
                print("No changes to commit.\n")
                return

            # Git push
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
