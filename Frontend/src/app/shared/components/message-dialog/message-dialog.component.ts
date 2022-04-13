import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-message-dialog',
  templateUrl: './message-dialog.component.html',
  styleUrls: ['./message-dialog.component.scss']
})
export class MessageDialogComponent implements OnInit {

  @Input() title: string = "Title";
  @Input() message: string = "Message";
  @Input() buttonText: string = "Button text";
  @Output() notify: EventEmitter<string> = new EventEmitter<string>(); 

  constructor() { }

  ngOnInit(): void {
  }

  closeDialog(): void{
    this.notify.emit('close');
  }

}
