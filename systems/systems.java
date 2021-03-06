import processing.core.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; public class systems extends PApplet {/*
    systems - Art computing piece.
    Copyright (C) 2007 Rob Myers rob@robmeyrs.org

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/    


// Configuration constants

int min_objects = 4;
int max_objects = 24;

// In pixels

int canvas_width = 400;
int canvas_height = 400;

int min_object_x = -100;
int max_object_x = 100;
int min_object_y = -100;
int max_object_y = 100;

float min_object_start_t = 0.0f;
float max_object_start_t = 0.5f;

int min_object_size = 5;
int max_object_size = 200;

// 0..255

int min_object_shade = 10;
int max_object_shade = 80;

// In seconds

float min_duration = 1.0f;
float max_duration = 10.0f;

// The object population

int num_objects;
float[] xs;
float[] ys;
float[] sizes;
float[] ts;
float[] t_scale_factors;
int[] shades;
float rotation;

// The current object population timeline

float start_growing;
float stop_growing;
float start_shrinking;
float stop_shrinking;

float grow_factor;
float shrink_factor;

// colours

int num_colours = 4;
int[] colours = new int [num_colours];

// Make the objects

public void gen_objects ()
{
  rotation = random (PI / 2.0f);
  
  num_objects = (int)random(min_objects, max_objects);
  
  xs = new float[num_objects]; 
  ys = new float[num_objects]; 
  ts = new float[num_objects];
  t_scale_factors = new float[num_objects];
  sizes = new float[num_objects]; 
  shades = new int[num_objects]; 
  
  for (int i = 0; i < num_objects; i++) 
  {
   xs[i] = random (min_object_x, max_object_x); 
   ys[i] = random (min_object_y, max_object_y); 
   ts[i] = random (min_object_start_t, max_object_start_t);
   t_scale_factors[i] = 1.0f / (1.0f - ts[i]);
   sizes[i] = random (min_object_size, max_object_size); 
   shades[i] = colours[(int)random (num_colours)]; 
  }
}

public float random_duration ()
{
 return random (min_duration, max_duration) * 1000; 
}

// make the timeline for the objects

public void gen_timeline ()
{
  start_growing = millis ();
  stop_growing = start_growing + random_duration ();
  start_shrinking = stop_growing + random_duration ();
  stop_shrinking = start_shrinking + random_duration ();
  
  grow_factor = 1.0f / (stop_growing - start_growing);
  shrink_factor = 1.0f / (stop_shrinking - start_shrinking);
}

public float scale_factor (float t)
{
 if (t > stop_shrinking)
 {
   return 0;
 } 
 else if (t > start_shrinking)
 {
   return 1.0f - ((t - start_shrinking) * shrink_factor);
 }
 else if (t > stop_growing)
 {
   return 1.0f;
 }
 else if (t > start_growing)
 {
  return (t - start_growing) * grow_factor; 
 }
  // So <= start_growing
  return  0.0f;
}
  
public void draw_object (int which, float scale_factor)
{
  if (scale_factor < ts[which])
  {
    return; 
  }
  float scale_scaled = (scale_factor - ts[which]) * t_scale_factors[which];
  float side_length = sizes[which] * scale_scaled;
  if (side_length > sizes[which])
  {
   side_length = sizes[which]; 
  }
  fill (shades[which]);
  rect (xs[which] * scale_scaled, ys[which] * scale_scaled, side_length, side_length);
}

public void draw_objects ()
{
  float now = millis ();
  float scale_factor = scale_factor (now);
  if (scale_factor == 0)
  {
   gen_objects ();
   gen_timeline ();
  }
  for (int i = 0; i < num_objects; i++)
  {
   draw_object (i, scale_factor);
  } 
}

public void setup_colours ()
{
  colours[0] = color (255, 0.0f, 0.0f, 240);
  colours[1] = color (255, 255, 0.0f, 200);
  colours[2] = color (0.0f, 0.0f, 255, 210);
  colours[3] = color (0.0f, 0.0f, 0.0f, 245);  
}

public void setup ()
{
  setup_colours ();
  size(canvas_width, canvas_height); 
  frameRate(30);
  gen_objects ();
  gen_timeline ();
}

public void draw ()
{
  background(255);
  translate (canvas_width / 2.0f, canvas_height / 2.0f);
  noStroke ();
  smooth ();
  rectMode (CENTER);
  draw_objects (); 
}
static public void main(String args[]) {   PApplet.main(new String[] { "systems" });}}