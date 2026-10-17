"""VidForge resource router."""

def select_resource(cpu_load, gpu_load):
    if gpu_load > cpu_load:
        return "GPU"
    if cpu_load > gpu_load:
        return "CPU"
    return "BALANCED"
