"""VidForge generic processing worker."""

def process_job(job_id, resource):
    return {"job_id": job_id, "resource": resource, "status": "PROCESSING"}
