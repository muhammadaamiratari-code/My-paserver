"""VidForge generation worker."""

from database.connection import get_connection


def process_next_job():
    db = get_connection()

    job = db.execute(
        "SELECT id FROM jobs WHERE status = 'QUEUED' ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'NORMAL' THEN 2 WHEN 'LOW' THEN 3 ELSE 4 END, id LIMIT 1"
    ).fetchone()

    if job is None:
        db.close()
        return None

    db.execute(
        "UPDATE jobs SET status = 'GENERATING' WHERE id = ?",
        (job["id"],),
    )
    db.commit()
    db.close()

    return job["id"]


def complete_job(job_id):
    db = get_connection()
    db.execute("UPDATE jobs SET status = 'COMPLETED', completed_at = CURRENT_TIMESTAMP WHERE id = ? AND status = 'GENERATING'", (job_id,))
    db.commit()
    db.close()


def fail_job(job_id):
    db = get_connection()
    db.execute("UPDATE jobs SET status = 'FAILED' WHERE id = ? AND status = 'GENERATING'", (job_id,))
    db.commit()
    db.close()


def retry_job(job_id):
    db = get_connection()
    db.execute("UPDATE jobs SET status = 'QUEUED', retry_count = retry_count + 1 WHERE id = ? AND status = 'FAILED' AND retry_count < 2", (job_id,))
    db.commit()
    db.close()


if __name__ == "__main__":
    job_id = process_next_job()
    print("Processed Job:", job_id)
