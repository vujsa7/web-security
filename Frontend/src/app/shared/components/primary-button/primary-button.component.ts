import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-primary-button',
  templateUrl: './primary-button.component.html',
  styleUrls: ['./primary-button.component.scss']
})
export class PrimaryButtonComponent {

  constructor() { }

  @Input() text: string = "Button";
  @Input() disabled: boolean = false;

}