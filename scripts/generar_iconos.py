"""Genera los íconos Android desde Logo/logo-ax100-mezcla-2t.png.

Requiere Pillow: python scripts/generar_iconos.py
"""
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parent.parent
original = Image.open(ROOT / "Logo/logo-ax100-mezcla-2t.png").convert("RGBA")
original = original.crop(original.getbbox())


def colocar(lienzo, lado):
    logo = original.copy()
    logo.thumbnail((lado, lado), Image.Resampling.LANCZOS)
    lienzo.alpha_composite(
        logo, ((lienzo.width - logo.width) // 2, (lienzo.height - logo.height) // 2)
    )


for densidad, escala in [
    ("mdpi", 1), ("hdpi", 1.5), ("xhdpi", 2), ("xxhdpi", 3), ("xxxhdpi", 4)
]:
    destino = ROOT / f"app/src/main/res/mipmap-{densidad}"
    destino.mkdir(parents=True, exist_ok=True)
    # Un cuadrado de 46 dp cabe dentro del círculo seguro de 66 dp.
    lado = round(108 * escala)
    adaptativo = Image.new("RGBA", (lado, lado))
    colocar(adaptativo, round(46 * escala))
    adaptativo.save(destino / "ax100_foreground.webp", lossless=True)

    lado = round(48 * escala)
    for redondo in (False, True):
        fondo = Image.new("RGBA", (lado, lado))
        dibujo = ImageDraw.Draw(fondo)
        if redondo:
            dibujo.ellipse((0, 0, lado - 1, lado - 1), fill="white")
        else:
            dibujo.rounded_rectangle(
                (0, 0, lado - 1, lado - 1), radius=lado // 5, fill="white"
            )
        colocar(fondo, round(lado * 0.68))
        nombre = "ic_launcher_round.webp" if redondo else "ic_launcher.webp"
        fondo.save(destino / nombre, lossless=True)
